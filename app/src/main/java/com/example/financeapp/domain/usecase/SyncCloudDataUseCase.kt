package com.example.financeapp.domain.usecase

import com.example.financeapp.data.AuthRepository
import com.example.financeapp.data.FinanceRepository
import kotlinx.coroutines.flow.firstOrNull

class SyncCloudDataUseCase(
    private val financeRepository: FinanceRepository,
    private val authRepository: AuthRepository
) {
    suspend fun executeFullSync(): Result<Unit> {
        return try {
            val uid = authRepository.getUid() ?: return Result.failure(Exception("User not authenticated"))
            val incomes = financeRepository.getAllIncomes().firstOrNull() ?: emptyList()
            val expenses = financeRepository.getAllExpenses().firstOrNull() ?: emptyList()
            val emis = financeRepository.getAllEmis().firstOrNull() ?: emptyList()
            val investments = financeRepository.getAllInvestments().firstOrNull() ?: emptyList()
            val accounts = financeRepository.getAllBankAccounts().firstOrNull() ?: emptyList()
            val cards = financeRepository.getAllCards().firstOrNull() ?: emptyList()

            incomes.forEach { financeRepository.syncToCloud(uid, it) }
            expenses.forEach { financeRepository.syncToCloud(uid, it) }
            emis.forEach { financeRepository.syncToCloud(uid, it) }
            investments.forEach { financeRepository.syncToCloud(uid, it) }
            accounts.forEach { financeRepository.syncToCloud(uid, it) }
            cards.forEach { financeRepository.syncToCloud(uid, it) }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
