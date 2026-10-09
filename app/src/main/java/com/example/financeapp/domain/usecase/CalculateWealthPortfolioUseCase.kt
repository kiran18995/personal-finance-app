package com.example.financeapp.domain.usecase

import com.example.financeapp.data.AccountRepository
import com.example.financeapp.data.EmiRepository
import com.example.financeapp.data.InvestmentRepository
import com.example.financeapp.domain.model.WealthPortfolioSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CalculateWealthPortfolioUseCase(
    private val investmentRepository: InvestmentRepository,
    private val accountRepository: AccountRepository,
    private val emiRepository: EmiRepository
) {
    operator fun invoke(): Flow<WealthPortfolioSummary> {
        return combine(
            investmentRepository.getAllInvestments(),
            accountRepository.getAllBankAccounts(),
            accountRepository.getAllCards(),
            emiRepository.getAllEmis()
        ) { investments, accounts, cards, emis ->
            val totalInvested = investments.sumOf { it.investedAmount }
            val currentValuation = investments.sumOf { it.currentValue }
            val totalSavings = accounts.sumOf { it.currentBalance }
            val totalCreditLimit = cards.filter { it.cardType == "Credit" }.sumOf { it.creditLimit }
            val totalDebt = cards.filter { it.cardType == "Credit" }.sumOf { it.outstandingBalance } + emis.sumOf { it.principal }

            val totalReturns = currentValuation - totalInvested
            val returnPercentage = if (totalInvested > 0) (totalReturns / totalInvested) * 100 else 0.0

            WealthPortfolioSummary(
                totalInvested = totalInvested,
                currentValuation = currentValuation,
                totalReturns = totalReturns,
                returnPercentage = returnPercentage,
                totalSavings = totalSavings,
                totalCreditLimit = totalCreditLimit,
                totalDebt = totalDebt
            )
        }
    }
}
