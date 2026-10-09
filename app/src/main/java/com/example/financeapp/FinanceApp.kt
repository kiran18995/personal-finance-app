package com.example.financeapp

import android.app.Application
import com.example.financeapp.data.AccountRepository
import com.example.financeapp.data.AppDatabase
import com.example.financeapp.data.AuthRepository
import com.example.financeapp.data.AuthRepositoryImpl
import com.example.financeapp.data.EmiRepository
import com.example.financeapp.data.ExpenseRepository
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.FinanceRepositoryImpl
import com.example.financeapp.data.FirestoreSyncService
import com.example.financeapp.data.IncomeRepository
import com.example.financeapp.data.InvestmentRepository
import com.example.financeapp.domain.usecase.CalculateWealthPortfolioUseCase
import com.example.financeapp.domain.usecase.GetDashboardOverviewUseCase
import com.example.financeapp.domain.usecase.ManageTransactionUseCase
import com.example.financeapp.domain.usecase.SyncCloudDataUseCase
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class FinanceApp : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: FinanceRepository
        private set
    lateinit var authRepository: AuthRepository
        private set

    val incomeRepository: IncomeRepository get() = repository
    val expenseRepository: ExpenseRepository get() = repository
    val emiRepository: EmiRepository get() = repository
    val investmentRepository: InvestmentRepository get() = repository
    val accountRepository: AccountRepository get() = repository

    lateinit var getDashboardOverviewUseCase: GetDashboardOverviewUseCase
        private set
    lateinit var calculateWealthPortfolioUseCase: CalculateWealthPortfolioUseCase
        private set
    lateinit var manageTransactionUseCase: ManageTransactionUseCase
        private set
    lateinit var syncCloudDataUseCase: SyncCloudDataUseCase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        val authImpl = AuthRepositoryImpl()
        authRepository = authImpl
        val financeImpl = FinanceRepositoryImpl(database, FirestoreSyncService(), authRepository)
        repository = financeImpl

        getDashboardOverviewUseCase = GetDashboardOverviewUseCase(repository)
        calculateWealthPortfolioUseCase = CalculateWealthPortfolioUseCase(
            investmentRepository = repository,
            accountRepository = repository,
            emiRepository = repository
        )
        manageTransactionUseCase = ManageTransactionUseCase(
            incomeRepository = repository,
            expenseRepository = repository
        )
        syncCloudDataUseCase = SyncCloudDataUseCase(repository, authRepository)

        PDFBoxResourceLoader.init(this)
    }
}
