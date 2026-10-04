package com.example.financeapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class FinanceRepository(
    private val db: AppDatabase,
    private val syncService: FirestoreSyncService,
    private val authRepository: AuthRepository
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    // ─── Income ──────────────────────────────────────────────────────
    fun getAllIncomes(): Flow<List<Income>> = db.incomeDao().getAll()
    fun getIncomeTotalSince(dateMs: Long): Flow<Double> = db.incomeDao().getTotalSince(dateMs)

    suspend fun addIncome(income: Income) {
        db.incomeDao().insert(income)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncIncome(it, income) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteIncome(income: Income) = db.incomeDao().delete(income)

    // ─── Expense ─────────────────────────────────────────────────────
    fun getAllExpenses(): Flow<List<Expense>> = db.expenseDao().getAll()
    fun getExpenseTotalSince(dateMs: Long): Flow<Double> = db.expenseDao().getTotalSince(dateMs)
    fun getCategoryTotals(sinceDateMs: Long): Flow<List<CategoryTotal>> =
        db.expenseDao().getCategoryTotals(sinceDateMs)

    suspend fun addExpense(expense: Expense) {
        db.expenseDao().insert(expense)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncExpense(it, expense) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteExpense(expense: Expense) = db.expenseDao().delete(expense)

    // ─── EMI ─────────────────────────────────────────────────────────
    fun getAllEmis(): Flow<List<Emi>> = db.emiDao().getAll()
    fun getTotalMonthlyEmi(): Flow<Double> = db.emiDao().getTotalMonthlyEmi()

    suspend fun addEmi(emi: Emi) {
        db.emiDao().insert(emi)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncEmi(it, emi) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteEmi(emi: Emi) = db.emiDao().delete(emi)

    // ─── Investment ──────────────────────────────────────────────────
    fun getAllInvestments(): Flow<List<Investment>> = db.investmentDao().getAll()
    fun getTotalInvestmentValue(): Flow<Double> = db.investmentDao().getTotalValue()

    suspend fun addInvestment(investment: Investment) {
        db.investmentDao().insert(investment)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncInvestment(it, investment) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteInvestment(investment: Investment) = db.investmentDao().delete(investment)

    // ─── Bank Account ────────────────────────────────────────────────
    fun getAllBankAccounts(): Flow<List<BankAccount>> = db.bankAccountDao().getAll()
    fun getTotalBankBalance(): Flow<Double> = db.bankAccountDao().getTotalBalance()

    suspend fun addBankAccount(account: BankAccount) {
        db.bankAccountDao().insert(account)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncBankAccount(it, account) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteBankAccount(account: BankAccount) = db.bankAccountDao().delete(account)

    // ─── Card ────────────────────────────────────────────────────────
    fun getAllCards(): Flow<List<Card>> = db.cardDao().getAll()

    suspend fun addCard(card: Card) {
        db.cardDao().insert(card)
        scope.launch {
            try { authRepository.getUid()?.let { syncService.syncCard(it, card) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteCard(card: Card) = db.cardDao().delete(card)

    // ─── Background sync helpers (used by SyncWorker) ────────────────
    /**
     * Generic overloaded entry points so [SyncWorker] can call a single
     * `syncToCloud(uid, entity)` without knowing the concrete type.
     */
    suspend fun syncToCloud(uid: String, income: Income) = syncService.syncIncome(uid, income)
    suspend fun syncToCloud(uid: String, expense: Expense) = syncService.syncExpense(uid, expense)
    suspend fun syncToCloud(uid: String, emi: Emi) = syncService.syncEmi(uid, emi)
    suspend fun syncToCloud(uid: String, investment: Investment) = syncService.syncInvestment(uid, investment)
    suspend fun syncToCloud(uid: String, account: BankAccount) = syncService.syncBankAccount(uid, account)
    suspend fun syncToCloud(uid: String, card: Card) = syncService.syncCard(uid, card)
}
