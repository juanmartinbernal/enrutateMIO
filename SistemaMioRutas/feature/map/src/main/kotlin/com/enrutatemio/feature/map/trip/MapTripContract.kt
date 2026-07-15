package com.enrutatemio.feature.map.trip

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.DirectionsResult
import com.enrutatemio.core.model.Station

/** Punto de referencia por defecto: Santiago de Cali (migrado de `MapGoogle.onMapReady()`). */
val CALI_DEFAULT_CENTER = Coordinates(latitude = 3.423507, longitude = -76.519046)
const val DEFAULT_ZOOM = 12f

data class MapTripState(
    val stations: List<Station> = emptyList(),
    val origin: Coordinates? = null,
    val destination: Coordinates? = null,
    val directions: DirectionsResult? = null,
    val isHybridMap: Boolean = false,
    val isLoadingDirections: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val hasRoute: Boolean get() = directions != null
}

sealed interface MapTripIntent : UiIntent {
    data class MapTapped(val coordinates: Coordinates) : MapTripIntent
    data object ClearRoute : MapTripIntent
    data object ToggleMapType : MapTripIntent
}

sealed interface MapTripEffect : UiEffect {
    data class ShowError(val message: String) : MapTripEffect
}
