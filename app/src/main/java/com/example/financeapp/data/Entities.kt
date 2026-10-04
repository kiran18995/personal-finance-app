package com.example.financeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val source: String, // Salary, Freelance, Business, Interest, Rental, Other
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String, // Food, Shopping, Transport, Bills, Entertainment, Health, Education, Other
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "emi")
data class Emi(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,          // e.g. "Home Loan", "Car Loan"
    val principal: Double,
    val interestRate: Double,  // annual %
    val termMonths: Int,
    val emiAmount: Double,     // monthly EMI
    val startDate: Long,
    val nextDueDate: Long,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "investment")
data class Investment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,          // e.g. "PPF", "Mutual Fund", "FD", "Stocks"
    val type: String,          // SIP, Lumpsum, FD, PPF, NPS, Stocks, Gold
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
    val accountType: String = "Savings", // Savings, Current, Salary
    val currentBalance: Double,
    val branchName: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "card")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardHolderName: String,
    val cardNumber: String,       // Last 4 digits only for display
    val cardType: String,         // "Credit" or "Debit"
    val cardNetwork: String = "", // Visa, Mastercard, RuPay, Amex
    val bankName: String = "",
    val expiryMonth: Int,
    val expiryYear: Int,
    val creditLimit: Double = 0.0,     // Only for credit cards
    val outstandingBalance: Double = 0.0, // Only for credit cards
    val linkedAccountId: Long? = null, // Link debit card to bank account
    val addedOn: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)
