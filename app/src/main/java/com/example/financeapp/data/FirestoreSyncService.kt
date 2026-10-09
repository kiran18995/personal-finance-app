package com.example.financeapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreSyncService : CloudSyncDataSource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun syncBankAccount(uid: String, account: BankAccount) {
        db.collection("users").document(uid)
            .collection("bank_accounts").document(account.id.toString())
            .set(account).await()
    }

    override suspend fun syncCard(uid: String, card: Card) {
        db.collection("users").document(uid)
            .collection("cards").document(card.id.toString())
            .set(card).await()
    }

    override suspend fun syncIncome(uid: String, income: Income) {
        db.collection("users").document(uid)
            .collection("incomes").document(income.id.toString())
            .set(income).await()
    }

    override suspend fun syncExpense(uid: String, expense: Expense) {
        db.collection("users").document(uid)
            .collection("expenses").document(expense.id.toString())
            .set(expense).await()
    }

    override suspend fun syncEmi(uid: String, emi: Emi) {
        db.collection("users").document(uid)
            .collection("emis").document(emi.id.toString())
            .set(emi).await()
    }

    override suspend fun syncInvestment(uid: String, investment: Investment) {
        db.collection("users").document(uid)
            .collection("investments").document(investment.id.toString())
            .set(investment).await()
    }
}
