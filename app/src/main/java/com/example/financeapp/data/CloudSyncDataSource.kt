package com.example.financeapp.data

/**
 * Abstraction for remote cloud synchronization (DIP: High-level repositories depend on this interface).
 */
interface CloudSyncDataSource {
    suspend fun syncBankAccount(uid: String, account: BankAccount)
    suspend fun syncCard(uid: String, card: Card)
    suspend fun syncIncome(uid: String, income: Income)
    suspend fun syncExpense(uid: String, expense: Expense)
    suspend fun syncEmi(uid: String, emi: Emi)
    suspend fun syncInvestment(uid: String, investment: Investment)
}
