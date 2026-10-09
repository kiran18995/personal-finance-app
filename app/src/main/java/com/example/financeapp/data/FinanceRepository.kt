package com.example.financeapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

// ─── Segregated Domain Repository Interfaces (Interface Segregation Principle) ───

interface IncomeRepository {
    fun getAllIncomes(): Flow<List<Income>>
    fun getIncomeTotalSince(dateMs: Long): Flow<Double>
    suspend fun addIncome(income: Income)
    suspend fun deleteIncome(income: Income)
}

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpenseTotalSince(dateMs: Long): Flow<Double>
    fun getCategoryTotals(sinceDateMs: Long): Flow<List<CategoryTotal>>
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}

interface EmiRepository {
    fun getAllEmis(): Flow<List<Emi>>
    fun getTotalMonthlyEmi(): Flow<Double>
    suspend fun addEmi(emi: Emi)
    suspend fun deleteEmi(emi: Emi)
}

interface InvestmentRepository {
    fun getAllInvestments(): Flow<List<Investment>>
    fun getTotalInvestmentValue(): Flow<Double>
    suspend fun addInvestment(investment: Investment)
    suspend fun deleteInvestment(investment: Investment)
}

interface AccountRepository {
    fun getAllBankAccounts(): Flow<List<BankAccount>>
    fun getTotalBankBalance(): Flow<Double>
    suspend fun addBankAccount(account: BankAccount)
    suspend fun deleteBankAccount(account: BankAccount)

    fun getAllCards(): Flow<List<Card>>
    suspend fun addCard(card: Card)
    suspend fun deleteCard(card: Card)
}

interface SyncRepository {
    suspend fun syncToCloud(uid: String, income: Income)
    suspend fun syncToCloud(uid: String, expense: Expense)
    suspend fun syncToCloud(uid: String, emi: Emi)
    suspend fun syncToCloud(uid: String, investment: Investment)
    suspend fun syncToCloud(uid: String, account: BankAccount)
    suspend fun syncToCloud(uid: String, card: Card)
}

/**
 * Composite FinanceRepository combining all segregated contracts.
 */
interface FinanceRepository : 
    IncomeRepository, 
    ExpenseRepository, 
    EmiRepository, 
    InvestmentRepository, 
    AccountRepository, 
    SyncRepository

class FinanceRepositoryImpl(
    private val db: AppDatabase,
    private val syncDataSource: CloudSyncDataSource,
    private val authRepository: AuthRepository
) : FinanceRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    // ─── Income ──────────────────────────────────────────────────────
    override fun getAllIncomes(): Flow<List<Income>> = db.incomeDao().getAll()
    override fun getIncomeTotalSince(dateMs: Long): Flow<Double> = db.incomeDao().getTotalSince(dateMs)

    override suspend fun addIncome(income: Income) {
        db.incomeDao().insert(income)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncIncome(it, income) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteIncome(income: Income) = db.incomeDao().delete(income)

    // ─── Expense ─────────────────────────────────────────────────────
    override fun getAllExpenses(): Flow<List<Expense>> = db.expenseDao().getAll()
    override fun getExpenseTotalSince(dateMs: Long): Flow<Double> = db.expenseDao().getTotalSince(dateMs)
    override fun getCategoryTotals(sinceDateMs: Long): Flow<List<CategoryTotal>> =
        db.expenseDao().getCategoryTotals(sinceDateMs)

    override suspend fun addExpense(expense: Expense) {
        db.expenseDao().insert(expense)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncExpense(it, expense) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteExpense(expense: Expense) = db.expenseDao().delete(expense)

    // ─── EMI ─────────────────────────────────────────────────────────
    override fun getAllEmis(): Flow<List<Emi>> = db.emiDao().getAll()
    override fun getTotalMonthlyEmi(): Flow<Double> = db.emiDao().getTotalMonthlyEmi()

    override suspend fun addEmi(emi: Emi) {
        db.emiDao().insert(emi)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncEmi(it, emi) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteEmi(emi: Emi) = db.emiDao().delete(emi)

    // ─── Investment ──────────────────────────────────────────────────
    override fun getAllInvestments(): Flow<List<Investment>> = db.investmentDao().getAll()
    override fun getTotalInvestmentValue(): Flow<Double> = db.investmentDao().getTotalValue()

    override suspend fun addInvestment(investment: Investment) {
        db.investmentDao().insert(investment)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncInvestment(it, investment) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteInvestment(investment: Investment) = db.investmentDao().delete(investment)

    // ─── Bank Account ────────────────────────────────────────────────
    override fun getAllBankAccounts(): Flow<List<BankAccount>> = db.bankAccountDao().getAll()
    override fun getTotalBankBalance(): Flow<Double> = db.bankAccountDao().getTotalBalance()

    override suspend fun addBankAccount(account: BankAccount) {
        db.bankAccountDao().insert(account)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncBankAccount(it, account) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteBankAccount(account: BankAccount) = db.bankAccountDao().delete(account)

    // ─── Card ────────────────────────────────────────────────────────
    override fun getAllCards(): Flow<List<Card>> = db.cardDao().getAll()

    override suspend fun addCard(card: Card) {
        db.cardDao().insert(card)
        scope.launch {
            try { authRepository.getUid()?.let { syncDataSource.syncCard(it, card) } } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun deleteCard(card: Card) = db.cardDao().delete(card)

    // ─── Background sync helpers ──────────────────────────────────────
    override suspend fun syncToCloud(uid: String, income: Income) = syncDataSource.syncIncome(uid, income)
    override suspend fun syncToCloud(uid: String, expense: Expense) = syncDataSource.syncExpense(uid, expense)
    override suspend fun syncToCloud(uid: String, emi: Emi) = syncDataSource.syncEmi(uid, emi)
    override suspend fun syncToCloud(uid: String, investment: Investment) = syncDataSource.syncInvestment(uid, investment)
    override suspend fun syncToCloud(uid: String, account: BankAccount) = syncDataSource.syncBankAccount(uid, account)
    override suspend fun syncToCloud(uid: String, card: Card) = syncDataSource.syncCard(uid, card)
}
