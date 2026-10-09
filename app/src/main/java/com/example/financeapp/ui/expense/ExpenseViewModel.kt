package com.example.financeapp.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.Expense
import com.example.financeapp.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalExpense: Double = 0.0,
    val isLoading: Boolean = false
)

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val uiState: StateFlow<ExpenseUiState> = expenseRepository.getAllExpenses()
        .map { list ->
            ExpenseUiState(
                expenses = list,
                totalExpense = list.sumOf { it.amount },
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExpenseUiState(isLoading = true)
        )

    fun addExpense(category: String, amount: Double, date: Long, description: String = "") {
        viewModelScope.launch {
            expenseRepository.addExpense(
                Expense(
                    category = category,
                    amount = amount,
                    date = date,
                    description = description
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expense)
        }
    }
}
