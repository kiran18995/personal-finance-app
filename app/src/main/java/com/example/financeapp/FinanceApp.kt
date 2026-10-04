package com.example.financeapp

import android.app.Application
import com.example.financeapp.data.AppDatabase
import com.example.financeapp.data.AuthRepository
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.FirestoreSyncService
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class FinanceApp : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: FinanceRepository
        private set
    lateinit var authRepository: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        authRepository = AuthRepository()
        repository = FinanceRepository(database, FirestoreSyncService(), authRepository)
        PDFBoxResourceLoader.init(this)
    }
}
