package com.example.financeapp.ui.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.Income
import com.example.financeapp.data.IncomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class IncomeUiState(
    val incomes: List<Income> = emptyList(),
    val totalIncome: Double = 0.0,
    val isLoading: Boolean = false
)

class IncomeViewModel(
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    val uiState: StateFlow<IncomeUiState> = incomeRepository.getAllIncomes()
        .map { list ->
            IncomeUiState(
                incomes = list,
                totalIncome = list.sumOf { it.amount },
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IncomeUiState(isLoading = true)
        )

    fun addIncome(source: String, amount: Double, date: Long, description: String = "") {
        viewModelScope.launch {
            incomeRepository.addIncome(
                Income(
                    source = source,
                    amount = amount,
                    date = date,
                    description = description
                )
            )
        }
    }

    fun deleteIncome(income: Income) {
        viewModelScope.launch {
            incomeRepository.deleteIncome(income)
        }
    }
}
