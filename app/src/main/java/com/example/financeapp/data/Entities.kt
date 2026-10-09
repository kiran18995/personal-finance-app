package com.example.financeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    // Salary, Freelance, Business, Interest, Rental, Other
    val source: String,
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    // Food, Shopping, Transport, Bills, Entertainment, Health, Education, Other
    val category: String,
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "emi")
data class Emi(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // e.g. "Home Loan", "Car Loan"
    val name: String,
    val principal: Double,
    // annual %
    val interestRate: Double,
    val termMonths: Int,
    // monthly EMI
    val emiAmount: Double,
    val startDate: Long,
    val nextDueDate: Long,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "investment")
data class Investment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // e.g. "PPF", "Mutual Fund", "FD", "Stocks"
    val name: String,
    // SIP, Lumpsum, FD, PPF, NPS, Stocks, Gold
    val type: String,
    val investedAmount: Double,
    val currentValue: Double,
    val startDate: Long,
    val maturityDate: Long? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "bank_account")
data class BankAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bankName: String,
    val accountHolderName: String = "",
    val accountNumber: String = "",
    val ifscCode: String = "",
    // Savings, Current, Salary
    val accountType: String = "Savings",
    val currentBalance: Double,
    val branchName: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "card")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardHolderName: String,
    // Last 4 digits only for display
    val cardNumber: String,
    // "Credit" or "Debit"
    val cardType: String,
    // Visa, Mastercard, RuPay, Amex
    val cardNetwork: String = "",
    val bankName: String = "",
    val expiryMonth: Int,
    val expiryYear: Int,
    // Only for credit cards
    val creditLimit: Double = 0.0,
    // Only for credit cards
    val outstandingBalance: Double = 0.0,
    // Link debit card to bank account
    val linkedAccountId: Long? = null,
    val addedOn: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)
