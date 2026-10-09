package com.example.financeapp.data

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

data class ParsedTransaction(
    val date: Long,
    val description: String,
    val amount: Double,
    val isIncome: Boolean
)

object BankStatementRegexPattern {
    // Format 1: Generic format with CR/DR at the end
    // Date, Description, Amount, CR/DR
    val genericFormat1 = Regex("""^(\d{2}/\d{2}/\d{4})\s+(.+?)\s+([\d,]+\.\d{2})\s+(CR|DR)$""", RegexOption.MULTILINE)
    
    // Format 2: HDFC Bank / Similar format
    // Date (dd/MM/yy), Narration+Ref, Value Date (dd/MM/yy), Amount, Closing Balance
    val hdfcFormat = Regex("""^(\d{2}/\d{2}/\d{2}(?:\d{2})?)\s+(.+?)\s+(\d{2}/\d{2}/\d{2}(?:\d{2})?)\s+([\d,]+\.\d{2})\s+([\d,]+\.\d{2})$""", RegexOption.MULTILINE)

    fun parseDate(dateStr: String): Long {
        val formatStr = if (dateStr.length == 8) "dd/MM/yy" else "dd/MM/yyyy"
        return try {
            val format = SimpleDateFormat(formatStr, Locale.getDefault())
            format.parse(dateStr)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    fun guessIsIncome(narration: String): Boolean {
        val upper = narration.uppercase()
        if (upper.contains("CASH DEP") || upper.contains("CREDIT INTEREST") || 
            upper.contains("SALARY") || upper.contains("ACH C-") || upper.contains("FT - CR")) {
            return true
        }
        if (upper.contains("DR-") || upper.contains("EMI") || upper.contains("POS ") || 
            upper.contains("NWD-") || upper.contains("ACH D-") || upper.contains("BILLPAY")) {
            return false
        }
        // Default to false (expense) for unknown
        return false
    }
}

interface StatementParser {
    suspend fun parsePdf(uri: Uri, password: String? = null): PdfParserService.ParsedStatementResult
}

class PdfParserService(private val context: Context) : StatementParser {

    data class ParsedStatementResult(
        val transactions: List<ParsedTransaction>,
        val finalBalance: Double?
    )

    override suspend fun parsePdf(uri: Uri, password: String?): ParsedStatementResult = withContext(Dispatchers.IO) {
        val transactions = mutableListOf<ParsedTransaction>()
        var finalBalance: Double? = null
        var document: PDDocument? = null
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                document = if (!password.isNullOrEmpty()) {
                    PDDocument.load(inputStream, password)
                } else {
                    PDDocument.load(inputStream)
                }
                
                val pdfStripper = PDFTextStripper()
                pdfStripper.sortByPosition = true
                val text = pdfStripper.getText(document)
                
                // Try Format 1
                val matchesFormat1 = BankStatementRegexPattern.genericFormat1.findAll(text).toList()
                if (matchesFormat1.isNotEmpty()) {
                    for (match in matchesFormat1) {
                        val dateStr = match.groupValues[1].trim()
                        val description = match.groupValues[2].trim()
                        val amountStr = match.groupValues[3].replace(",", "")
                        val type = match.groupValues[4].trim()
                        
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            transactions.add(
                                ParsedTransaction(
                                    date = BankStatementRegexPattern.parseDate(dateStr),
                                    description = description,
                                    amount = amount,
                                    isIncome = type == "CR"
                                )
                            )
                        }
                    }
                } else {
                    // Try Format 2 (HDFC)
                    val matchesFormat2 = BankStatementRegexPattern.hdfcFormat.findAll(text).toList()
                    var prevBalance: Double? = null
                    for (match in matchesFormat2) {
                        val dateStr = match.groupValues[1].trim()
                        val description = match.groupValues[2].trim()
                        val amountStr = match.groupValues[4].replace(",", "")
                        val balanceStr = match.groupValues[5].replace(",", "")
                        
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        val balance = balanceStr.toDoubleOrNull() ?: 0.0
                        
                        if (amount > 0) {
                            val isIncome = if (prevBalance != null) {
                                balance > prevBalance
                            } else {
                                BankStatementRegexPattern.guessIsIncome(description)
                            }
                            
                            transactions.add(
                                ParsedTransaction(
                                    date = BankStatementRegexPattern.parseDate(dateStr),
                                    description = description,
                                    amount = amount,
                                    isIncome = isIncome
                                )
                            )
                        }
                        prevBalance = balance
                        finalBalance = balance
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle incorrect password or IO exception
            throw e
        } finally {
            document?.close()
        }
        
        ParsedStatementResult(transactions, finalBalance)
    }
}
