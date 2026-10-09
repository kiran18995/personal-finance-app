package com.example.financeapp.domain.usecase

import com.example.financeapp.data.Expense
import com.example.financeapp.data.ExpenseRepository
import com.example.financeapp.data.Income
import com.example.financeapp.data.IncomeRepository

class ManageTransactionUseCase(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
) {
    suspend fun recordIncome(income: Income) {
        incomeRepository.addIncome(income)
    }

    suspend fun recordExpense(expense: Expense) {
        expenseRepository.addExpense(expense)
    }

    suspend fun deleteIncome(income: Income) {
        incomeRepository.deleteIncome(income)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseRepository.deleteExpense(expense)
    }
}
