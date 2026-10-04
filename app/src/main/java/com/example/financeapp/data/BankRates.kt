package com.example.financeapp.data

data class BankRate(
    val bankName: String,
    val loanType: String,
    val minRate: Double,
    val maxRate: Double,
    val lastUpdated: String
)

object BankRatesRepository {
    fun getBangaloreRates(): List<BankRate> {
        return listOf(
            BankRate("SBI", "Home Loan", 8.40, 10.15, "Today"),
            BankRate("HDFC Bank", "Home Loan", 8.50, 9.40, "Today"),
            BankRate("ICICI Bank", "Home Loan", 8.75, 9.65, "Today"),
            BankRate("Axis Bank", "Home Loan", 8.75, 9.10, "Today"),
            BankRate("Kotak Mahindra", "Home Loan", 8.70, 9.35, "Today"),
            BankRate("Bank of Baroda", "Home Loan", 8.40, 10.60, "Today"),
            BankRate("SBI", "Personal Loan", 11.15, 14.30, "Today"),
            BankRate("HDFC Bank", "Personal Loan", 10.50, 21.00, "Today"),
            BankRate("ICICI Bank", "Personal Loan", 10.75, 19.00, "Today"),
            BankRate("Axis Bank", "Personal Loan", 10.49, 21.00, "Today"),
            BankRate("Bajaj Finserv", "Personal Loan", 11.00, 32.00, "Today")
        )
    }
}
