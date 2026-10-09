package com.example.financeapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.BankAccount
import com.example.financeapp.data.Card
import com.example.financeapp.data.CategoryTotal
import com.example.financeapp.data.Emi
import com.example.financeapp.data.Expense
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.Income
import com.example.financeapp.domain.usecase.ManageTransactionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

enum class TimeRange {
    THIS_MONTH,
    LAST_3_MONTHS,
    ALL_TIME
}

/** Returns the start-of-period timestamp in ms for the given [TimeRange]. */
private fun TimeRange.toStartEpochMs(): Long = when (this) {
    TimeRange.THIS_MONTH -> Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    TimeRange.LAST_3_MONTHS -> Calendar.getInstance().apply {
        add(Calendar.MONTH, -3)
    }.timeInMillis
    TimeRange.ALL_TIME -> 0L
}

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(
    private val repo: FinanceRepository,
    private val manageTransactionUseCase: ManageTransactionUseCase = ManageTransactionUseCase(repo, repo)
) : ViewModel() {

    private val _timeRange = MutableStateFlow(TimeRange.THIS_MONTH)
    val timeRange: StateFlow<TimeRange> = _timeRange.asStateFlow()

    fun setTimeRange(range: TimeRange) {
        _timeRange.value = range
    }

    private val startDateFlow: Flow<Long> = _timeRange.map { it.toStartEpochMs() }

    val totalIncome: StateFlow<Double> = startDateFlow
        .flatMapLatest { repo.getIncomeTotalSince(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val totalExpense: StateFlow<Double> = startDateFlow
        .flatMapLatest { repo.getExpenseTotalSince(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val totalEmi: StateFlow<Double> = repo.getTotalMonthlyEmi()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val totalInvestment: StateFlow<Double> = repo.getTotalInvestmentValue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val categoryTotals: StateFlow<List<CategoryTotal>> = startDateFlow
        .flatMapLatest { repo.getCategoryTotals(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val upcomingEmis: StateFlow<List<Emi>> = repo.getAllEmis()
        .map { it.take(3) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val bankAccounts: StateFlow<List<BankAccount>> = repo.getAllBankAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cards: StateFlow<List<Card>> = repo.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val balance: StateFlow<Double> = repo.getTotalBankBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    // Simple recommendation engine
    val recommendations: StateFlow<List<String>> = combine(
        totalIncome, totalExpense, categoryTotals
    ) { inc, exp, cats ->
        buildList {
            if (inc > 0) {
                val ratio = exp / inc
                when {
                    ratio > 0.8 -> add(
                        "⚠️ You've spent ${(ratio * 100).toInt()}% of your income. Try to keep expenses below 80%."
                    )
                    ratio < 0.5 -> add("🎉 Great job! You're saving over 50% of your income.")
                }
                cats.firstOrNull()?.takeIf { it.total > inc * 0.3 }?.let { top ->
                    add("💡 ${top.category} is your biggest expense (${(top.total / inc * 100).toInt()}% of income). Consider reducing it by 10%.")
                }
            }
            if (isEmpty()) add("💰 Start by adding your income and expenses to get personalised tips!")
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        listOf("💰 Start by adding your income and expenses to get personalised tips!")
    )

    // Single source: all mapped transactions sorted by date (descending).
    // recentTransactions is a derived view — no extra DB subscriptions needed.
    private val allMappedTransactions: Flow<List<RecentTransaction>> = combine(
        repo.getAllIncomes(),
        repo.getAllExpenses()
    ) { incomes: List<Income>, expenses: List<Expense> ->
        val mapped = ArrayList<RecentTransaction>(incomes.size + expenses.size)
        incomes.mapTo(mapped) { RecentTransaction(it.id, it.source, "Income", it.amount, true, it.date, "Wallet") }
        expenses.mapTo(mapped) { RecentTransaction(it.id, it.category, it.description, it.amount, false, it.date, "Wallet") }
        mapped.sortedByDescending { it.date }
    }

    val allTransactions: StateFlow<List<RecentTransaction>> = allMappedTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recentTransactions: StateFlow<List<RecentTransaction>> = allMappedTransactions
        .map { it.take(5) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addTransaction(amount: Double, isIncome: Boolean, category: String, accountName: String) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (isIncome) {
                manageTransactionUseCase.recordIncome(Income(source = category, amount = amount, date = now))
            } else {
                manageTransactionUseCase.recordExpense(
                    Expense(
                        category = category,
                        amount = amount,
                        date = now,
                        description = "Added from $accountName"
                    )
                )
            }
        }
    }

    var selectedCard: Card? = null
    var selectedBankAccount: BankAccount? = null

    fun addBankAccount(account: BankAccount) {
        viewModelScope.launch { repo.addBankAccount(account) }
    }

    fun addCard(card: Card) {
        viewModelScope.launch { repo.addCard(card) }
    }
}

data class RecentTransaction(
    val id: Long,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: Long,
    val account: String
)
