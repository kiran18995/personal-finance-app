package com.example.financeapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreSyncService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun syncBankAccount(uid: String, account: BankAccount) {
        db.collection("users").document(uid)
            .collection("bank_accounts").document(account.id.toString())
            .set(account).await()
    }

    suspend fun syncCard(uid: String, card: Card) {
        db.collection("users").document(uid)
            .collection("cards").document(card.id.toString())
            .set(card).await()
    }

    suspend fun syncIncome(uid: String, income: Income) {
        db.collection("users").document(uid)
            .collection("incomes").document(income.id.toString())
            .set(income).await()
    }

    suspend fun syncExpense(uid: String, expense: Expense) {
        db.collection("users").document(uid)
            .collection("expenses").document(expense.id.toString())
            .set(expense).await()
    }

    suspend fun syncEmi(uid: String, emi: Emi) {
        db.collection("users").document(uid)
            .collection("emis").document(emi.id.toString())
            .set(emi).await()
    }

    suspend fun syncInvestment(uid: String, investment: Investment) {
        db.collection("users").document(uid)
            .collection("investments").document(investment.id.toString())
            .set(investment).await()
    }
}
