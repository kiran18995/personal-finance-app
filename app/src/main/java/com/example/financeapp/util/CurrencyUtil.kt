package com.example.financeapp.util

import java.text.DecimalFormat

/**
 * Formats a number as Indian Rupees: ₹1,23,456.78
 */
fun Double.toINR(): String {
    if (this < 0) return "-${(-this).toINR()}"
    val intPart = this.toLong()
    val decPart = ((this - intPart) * 100).toLong()

    val intStr = intPart.toString()
    val formatted = if (intStr.length <= 3) {
        intStr
    } else {
        val last3 = intStr.substring(intStr.length - 3)
        val rest = intStr.substring(0, intStr.length - 3)
        val grouped = StringBuilder()
        var i = rest.length
        while (i > 0) {
            val start = maxOf(0, i - 2)
            if (grouped.isNotEmpty()) grouped.insert(0, ",")
            grouped.insert(0, rest.substring(start, i))
            i = start
        }
        "$grouped,$last3"
    }
    return if (decPart > 0) "₹$formatted.${String.format("%02d", decPart)}"
    else "₹$formatted"
}

fun Double.toINRCompact(): String {
    return when {
        this >= 10000000 -> String.format("₹%.1fCr", this / 10000000)
        this >= 100000 -> String.format("₹%.1fL", this / 100000)
        this >= 1000 -> String.format("₹%.1fK", this / 1000)
        else -> toINR()
    }
}

val ExpenseCategories = listOf("Food", "Shopping", "Transport", "Bills", "Entertainment", "Health", "Education", "Other")
val IncomeSources = listOf("Salary", "Freelance", "Business", "Interest", "Rental", "Other")
val InvestmentTypes = listOf("SIP", "Lumpsum", "FD", "PPF", "NPS", "Stocks", "Gold", "Other")
