package com.example.financeapp.ui.statement

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.Income
import com.example.financeapp.data.Expense
import com.example.financeapp.data.ParsedTransaction
import com.example.financeapp.data.StatementParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StatementState {
    object Idle : StatementState()
    object Parsing : StatementState()
    data class RequiresPassword(val uri: Uri) : StatementState()
    data class Review(val transactions: List<ParsedTransaction>, val finalBalance: Double?) : StatementState()
    data class Error(val message: String) : StatementState()
    object Success : StatementState()
}

class StatementViewModel(
    private val repository: FinanceRepository,
    private val statementParser: StatementParser
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatementState>(StatementState.Idle)
    val uiState: StateFlow<StatementState> = _uiState

    fun parsePdf(uri: Uri, password: String? = null) {
        viewModelScope.launch {
            _uiState.value = StatementState.Parsing
            try {
                val result = statementParser.parsePdf(uri, password)
                _uiState.value = StatementState.Review(result.transactions, result.finalBalance)
            } catch (e: Exception) {
                // If wrong password
                if (e.message?.contains("password") == true || e.message?.contains("encrypt") == true) {
                    _uiState.value = StatementState.RequiresPassword(uri)
                } else {
                    _uiState.value = StatementState.Error(e.message ?: "Failed to parse PDF")
                }
            }
        }
    }

    fun saveTransactions(transactions: List<ParsedTransaction>, finalBalance: Double?) {
        viewModelScope.launch {
            transactions.forEach { txn ->
                if (txn.isIncome) {
                    repository.addIncome(
                        Income(
                            amount = txn.amount,
                            source = "Bank Statement",
                            date = txn.date,
                            description = txn.description
                        )
                    )
                } else {
                    repository.addExpense(
                        Expense(
                            amount = txn.amount,
                            category = "Bank Statement",
                            date = txn.date,
                            description = txn.description
                        )
                    )
                }
            }
            if (finalBalance != null) {
                repository.addBankAccount(
                    com.example.financeapp.data.BankAccount(
                        bankName = "Imported Account",
                        currentBalance = finalBalance
                    )
                )
            }
            _uiState.value = StatementState.Success
        }
    }
    
    fun reset() {
        _uiState.value = StatementState.Idle
    }
}
