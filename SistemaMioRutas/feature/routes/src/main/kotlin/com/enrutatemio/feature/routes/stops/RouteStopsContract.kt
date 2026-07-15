package com.enrutatemio.feature.routes.stops

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteStop

data class RouteStopsState(
    val routeCode: String = "",
    val direction: RouteDirection = RouteDirection.NORTE_SUR,
    val stops: List<RouteStop> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : UiState

sealed interface RouteStopsIntent : UiIntent {
    data object Retry : RouteStopsIntent
}

sealed interface RouteStopsEffect : UiEffect
