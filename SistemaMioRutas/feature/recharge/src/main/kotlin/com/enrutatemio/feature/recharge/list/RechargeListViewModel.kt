package com.enrutatemio.feature.recharge.list

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.domain.recharge.GetRechargePointsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RechargeListViewModel(
    private val getRechargePoints: GetRechargePointsUseCase,
) : BaseMviViewModel<RechargeListState, RechargeListIntent, RechargeListEffect>(RechargeListState()) {

    private var searchJob: Job? = null

    init {
        load(currentState.query)
    }

    override fun onIntent(intent: RechargeListIntent) {
        when (intent) {
            is RechargeListIntent.Search -> {
                setState { copy(query = intent.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(SEARCH_DEBOUNCE_MS)
                    load(intent.query)
                }
            }
            RechargeListIntent.Retry -> load(currentState.query)
        }
    }

    private fun load(query: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching { getRechargePoints(query) }
                .onSuccess { points -> setState { copy(isLoading = false, points = points) } }
                .onFailure { error ->
                    setState { copy(isLoading = false, errorMessage = error.message ?: "Error al cargar puntos de recarga") }
                }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
