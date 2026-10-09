package com.example.financeapp.ui.emi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.Emi
import com.example.financeapp.data.EmiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EmiUiState(
    val emis: List<Emi> = emptyList(),
    val totalMonthlyEmi: Double = 0.0,
    val totalPrincipal: Double = 0.0,
    val isLoading: Boolean = false
)

class EmiViewModel(
    private val emiRepository: EmiRepository
) : ViewModel() {

    val uiState: StateFlow<EmiUiState> = emiRepository.getAllEmis()
        .map { list ->
            EmiUiState(
                emis = list,
                totalMonthlyEmi = list.sumOf { it.emiAmount },
                totalPrincipal = list.sumOf { it.principal },
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EmiUiState(isLoading = true)
        )

    fun addEmi(
        name: String,
        principal: Double,
        interestRate: Double,
        termMonths: Int,
        emiAmount: Double,
        startDate: Long = System.currentTimeMillis(),
        nextDueDate: Long = System.currentTimeMillis() + 30L * 24 * 3600 * 1000
    ) {
        viewModelScope.launch {
            emiRepository.addEmi(
                Emi(
                    name = name,
                    principal = principal,
                    interestRate = interestRate,
                    termMonths = termMonths,
                    emiAmount = emiAmount,
                    startDate = startDate,
                    nextDueDate = nextDueDate
                )
            )
        }
    }

    fun deleteEmi(emi: Emi) {
        viewModelScope.launch {
            emiRepository.deleteEmi(emi)
        }
    }
}
