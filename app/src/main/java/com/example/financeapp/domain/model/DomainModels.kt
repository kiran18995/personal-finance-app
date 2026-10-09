package com.example.financeapp.domain.model

import com.example.financeapp.data.Expense
import com.example.financeapp.data.Income

data class DashboardSummary(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalBankBalance: Double = 0.0,
    val totalInvestments: Double = 0.0,
    val totalEmis: Double = 0.0,
    val totalCreditCardDebt: Double = 0.0,
    val netWorth: Double = 0.0,
    val recentIncomes: List<Income> = emptyList(),
    val recentExpenses: List<Expense> = emptyList()
)

data class WealthPortfolioSummary(
    val totalInvested: Double = 0.0,
    val currentValuation: Double = 0.0,
    val totalReturns: Double = 0.0,
    val returnPercentage: Double = 0.0,
    val totalSavings: Double = 0.0,
    val totalCreditLimit: Double = 0.0,
    val totalDebt: Double = 0.0
)
