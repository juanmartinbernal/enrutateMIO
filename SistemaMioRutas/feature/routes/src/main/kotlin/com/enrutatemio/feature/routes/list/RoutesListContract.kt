package com.enrutatemio.feature.routes.list

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.TransitRoute

data class RoutesListState(
    val selectedType: RouteType = RouteType.TRONCAL,
    val query: String = "",
    val routes: List<TransitRoute> = emptyList(),
    val expandedRouteCode: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val filteredRoutes: List<TransitRoute>
        get() = if (query.isBlank()) {
            routes
        } else {
            routes.filter { it.code.contains(query, ignoreCase = true) }
        }
}

sealed interface RoutesListIntent : UiIntent {
    data class SelectType(val type: RouteType) : RoutesListIntent
    data class Search(val query: String) : RoutesListIntent
    data class ToggleExpand(val routeCode: String) : RoutesListIntent
    data class SelectDirection(val routeCode: String, val direction: RouteDirection) : RoutesListIntent
    data object Retry : RoutesListIntent
}

sealed interface RoutesListEffect : UiEffect {
    data class NavigateToStops(val routeCode: String, val direction: RouteDirection) : RoutesListEffect
}
