package com.example.financeapp.domain.usecase

import com.example.financeapp.data.BankAccount
import com.example.financeapp.data.Card
import com.example.financeapp.data.Emi
import com.example.financeapp.data.Expense
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.Income
import com.example.financeapp.data.Investment
import com.example.financeapp.domain.model.DashboardSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDashboardOverviewUseCase(
    private val financeRepository: FinanceRepository
) {
    @Suppress("UNCHECKED_CAST")
    operator fun invoke(): Flow<DashboardSummary> {
        return combine(
            financeRepository.getAllIncomes(),
            financeRepository.getAllExpenses(),
            financeRepository.getAllBankAccounts(),
            financeRepository.getAllInvestments(),
            financeRepository.getAllEmis(),
            financeRepository.getAllCards()
        ) { flowsArray ->
            val incomes = flowsArray[0] as List<Income>
            val expenses = flowsArray[1] as List<Expense>
            val accounts = flowsArray[2] as List<BankAccount>
            val investments = flowsArray[3] as List<Investment>
            val emis = flowsArray[4] as List<Emi>
            val cards = flowsArray[5] as List<Card>

            val totalIncome = incomes.sumOf { it.amount }
            val totalExpense = expenses.sumOf { it.amount }
            val totalBankBalance = accounts.sumOf { it.currentBalance }
            val totalInvestments = investments.sumOf { it.currentValue }
            val totalEmis = emis.sumOf { it.emiAmount }
            val totalCreditCardDebt = cards.filter { it.cardType == "Credit" }.sumOf { it.outstandingBalance }
            val netWorth = (totalBankBalance + totalInvestments) - totalCreditCardDebt

            DashboardSummary(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                totalBankBalance = totalBankBalance,
                totalInvestments = totalInvestments,
                totalEmis = totalEmis,
                totalCreditCardDebt = totalCreditCardDebt,
                netWorth = netWorth,
                recentIncomes = incomes.take(5),
                recentExpenses = expenses.take(5)
            )
        }
    }
}
