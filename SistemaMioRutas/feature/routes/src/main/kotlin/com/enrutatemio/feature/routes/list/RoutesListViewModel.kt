package com.enrutatemio.feature.routes.list

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.domain.routes.GetRoutesByTypeUseCase
import kotlinx.coroutines.launch

class RoutesListViewModel(
    private val getRoutesByType: GetRoutesByTypeUseCase,
) : BaseMviViewModel<RoutesListState, RoutesListIntent, RoutesListEffect>(RoutesListState()) {

    init {
        loadRoutes(currentState.selectedType)
    }

    override fun onIntent(intent: RoutesListIntent) {
        when (intent) {
            is RoutesListIntent.SelectType -> {
                setState { copy(selectedType = intent.type, expandedRouteCode = null) }
                loadRoutes(intent.type)
            }
            is RoutesListIntent.Search -> setState { copy(query = intent.query) }
            is RoutesListIntent.ToggleExpand -> setState {
                copy(expandedRouteCode = if (expandedRouteCode == intent.routeCode) null else intent.routeCode)
            }
            is RoutesListIntent.SelectDirection -> sendEffect {
                RoutesListEffect.NavigateToStops(intent.routeCode, intent.direction)
            }
            RoutesListIntent.Retry -> loadRoutes(currentState.selectedType)
        }
    }

    private fun loadRoutes(type: RouteType) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching { getRoutesByType(type) }
                .onSuccess { routes -> setState { copy(isLoading = false, routes = routes) } }
                .onFailure { error ->
                    setState { copy(isLoading = false, errorMessage = error.message ?: "Error al cargar rutas") }
                }
        }
    }
}
