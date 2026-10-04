package com.example.financeapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.financeapp.FinanceApp
import kotlinx.coroutines.flow.first

/**
 * Periodic WorkManager worker that uploads all locally-stored records to Firestore.
 *
 * Uses the Application-level singleton [FinanceRepository] and [AuthRepository] so that
 * no Hilt / Koin DI framework is needed.
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val app = applicationContext as FinanceApp
            val repo = app.repository
            val authRepo = app.authRepository

            val uid = authRepo.getUid() ?: return Result.success() // Not signed in — nothing to sync

            // Upload all local data to Firestore
            val incomes = repo.getAllIncomes().first()
            incomes.forEach { repo.syncToCloud(uid, it) }

            val expenses = repo.getAllExpenses().first()
            expenses.forEach { repo.syncToCloud(uid, it) }

            val emis = repo.getAllEmis().first()
            emis.forEach { repo.syncToCloud(uid, it) }

            val investments = repo.getAllInvestments().first()
            investments.forEach { repo.syncToCloud(uid, it) }

            val accounts = repo.getAllBankAccounts().first()
            accounts.forEach { repo.syncToCloud(uid, it) }

            val cards = repo.getAllCards().first()
            cards.forEach { repo.syncToCloud(uid, it) }

            Result.success()
        } catch (e: Exception) {
            // Retry up to WorkManager's default retry limit
            Result.retry()
        }
    }
}
