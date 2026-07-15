package com.enrutatemio.feature.map.trip

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.core.datastore.UserPreferencesDataStore
import com.enrutatemio.domain.map.GetWalkingDirectionsUseCase
import com.enrutatemio.domain.routes.GetAllStationsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Planificador de viaje punto a punto sobre el mapa. Reemplaza al backend propio legacy
 * (`Service.connectByHTTP` hacia `tuyo.herokuapp.com`, hoy inactivo) por Google Directions API.
 * Flujo: primer tap = origen, segundo tap = destino y dispara el cálculo de la ruta caminando.
 */
class MapTripViewModel(
    private val getAllStations: GetAllStationsUseCase,
    private val getWalkingDirections: GetWalkingDirectionsUseCase,
    private val preferences: UserPreferencesDataStore,
) : BaseMviViewModel<MapTripState, MapTripIntent, MapTripEffect>(MapTripState()) {

    init {
        viewModelScope.launch {
            val hybrid = preferences.hybridMapEnabled.first()
            setState { copy(isHybridMap = hybrid) }
        }
        viewModelScope.launch {
            runCatching { getAllStations() }
                .onSuccess { stations -> setState { copy(stations = stations) } }
        }
    }

    override fun onIntent(intent: MapTripIntent) {
        when (intent) {
            is MapTripIntent.MapTapped -> handleMapTap(intent.coordinates)
            MapTripIntent.ClearRoute -> setState {
                copy(origin = null, destination = null, directions = null, errorMessage = null)
            }
            MapTripIntent.ToggleMapType -> {
                val newValue = !currentState.isHybridMap
                setState { copy(isHybridMap = newValue) }
                viewModelScope.launch { preferences.setHybridMapEnabled(newValue) }
            }
        }
    }

    private fun handleMapTap(point: com.enrutatemio.core.model.Coordinates) {
        val state = currentState
        when {
            state.origin == null -> setState { copy(origin = point, destination = null, directions = null) }
            state.destination == null -> {
                setState { copy(destination = point) }
                fetchDirections(state.origin, point)
            }
            else -> setState { copy(origin = point, destination = null, directions = null) }
        }
    }

    private fun fetchDirections(origin: com.enrutatemio.core.model.Coordinates, destination: com.enrutatemio.core.model.Coordinates) {
        viewModelScope.launch {
            setState { copy(isLoadingDirections = true, errorMessage = null) }
            runCatching { getWalkingDirections(origin, destination) }
                .onSuccess { result -> setState { copy(isLoadingDirections = false, directions = result) } }
                .onFailure { error ->
                    setState { copy(isLoadingDirections = false, errorMessage = error.message ?: "No se pudo calcular la ruta") }
                    sendEffect { MapTripEffect.ShowError(error.message ?: "No se pudo calcular la ruta") }
                }
        }
    }
}
