package com.enrutatemio.feature.routes.stops

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.domain.routes.GetRouteStopsUseCase
import kotlinx.coroutines.launch

class RouteStopsViewModel(
    private val routeCode: String,
    private val direction: RouteDirection,
    private val getRouteStops: GetRouteStopsUseCase,
) : BaseMviViewModel<RouteStopsState, RouteStopsIntent, RouteStopsEffect>(
    RouteStopsState(routeCode = routeCode, direction = direction),
) {

    init {
        load()
    }

    override fun onIntent(intent: RouteStopsIntent) {
        when (intent) {
            RouteStopsIntent.Retry -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching { getRouteStops(routeCode, direction) }
                .onSuccess { stops -> setState { copy(isLoading = false, stops = stops) } }
                .onFailure { error ->
                    setState { copy(isLoading = false, errorMessage = error.message ?: "Error al cargar paradas") }
                }
        }
    }
}
