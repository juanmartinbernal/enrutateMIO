package com.enrutatemio.feature.feeders.list

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone

data class FeedersState(
    val zone: Zone = Zone.TODAS,
    val query: String = "",
    val routes: List<TransitRoute> = emptyList(),
    val expandedRouteCode: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val filteredRoutes: List<TransitRoute>
        get() = if (query.isBlank()) routes else routes.filter { it.code.contains(query, ignoreCase = true) }
}

sealed interface FeedersIntent : UiIntent {
    data class SelectZone(val zone: Zone) : FeedersIntent
    data class Search(val query: String) : FeedersIntent
    data class ToggleExpand(val routeCode: String) : FeedersIntent
    data class SelectDirection(val routeCode: String, val direction: RouteDirection) : FeedersIntent
    data object Retry : FeedersIntent
}

sealed interface FeedersEffect : UiEffect {
    data class NavigateToStops(val routeCode: String, val direction: RouteDirection) : FeedersEffect
}
