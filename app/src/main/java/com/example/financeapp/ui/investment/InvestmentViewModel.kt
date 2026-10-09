package com.example.financeapp.ui.investment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.Investment
import com.example.financeapp.data.InvestmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InvestmentUiState(
    val investments: List<Investment> = emptyList(),
    val totalInvested: Double = 0.0,
    val totalCurrent: Double = 0.0,
    val totalGain: Double = 0.0,
    val returnPercent: Double = 0.0,
    val isLoading: Boolean = false
)

class InvestmentViewModel(
    private val investmentRepository: InvestmentRepository
) : ViewModel() {

    val uiState: StateFlow<InvestmentUiState> = investmentRepository.getAllInvestments()
        .map { list ->
            val totalInvested = list.sumOf { it.investedAmount }
            val totalCurrent = list.sumOf { it.currentValue }
            val totalGain = totalCurrent - totalInvested
            val returnPercent = if (totalInvested > 0) (totalGain / totalInvested) * 100 else 0.0
            InvestmentUiState(
                investments = list,
                totalInvested = totalInvested,
                totalCurrent = totalCurrent,
                totalGain = totalGain,
                returnPercent = returnPercent,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InvestmentUiState(isLoading = true)
        )

    fun addInvestment(name: String, type: String, investedAmount: Double, currentValue: Double) {
        viewModelScope.launch {
            investmentRepository.addInvestment(
                Investment(
                    name = name,
                    type = type,
                    investedAmount = investedAmount,
                    currentValue = currentValue,
                    startDate = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteInvestment(investment: Investment) {
        viewModelScope.launch {
            investmentRepository.deleteInvestment(investment)
        }
    }
}
