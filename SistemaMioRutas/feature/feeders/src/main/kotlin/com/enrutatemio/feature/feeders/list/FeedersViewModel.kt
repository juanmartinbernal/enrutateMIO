package com.enrutatemio.feature.feeders.list

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.core.datastore.UserPreferencesDataStore
import com.enrutatemio.core.model.Zone
import com.enrutatemio.domain.feeders.GetFeederRoutesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FeedersViewModel(
    private val getFeederRoutes: GetFeederRoutesUseCase,
    private val preferences: UserPreferencesDataStore,
) : BaseMviViewModel<FeedersState, FeedersIntent, FeedersEffect>(FeedersState()) {

    init {
        viewModelScope.launch {
            val lastZone = preferences.lastFeederZone.first()
            val zone = lastZone?.let { saved -> Zone.entries.firstOrNull { it.dbValue == saved } } ?: Zone.TODAS
            setState { copy(zone = zone) }
            loadFeeders(zone)
        }
    }

    override fun onIntent(intent: FeedersIntent) {
        when (intent) {
            is FeedersIntent.SelectZone -> {
                setState { copy(zone = intent.zone, expandedRouteCode = null) }
                viewModelScope.launch { preferences.setLastFeederZone(intent.zone.dbValue) }
                loadFeeders(intent.zone)
            }
            is FeedersIntent.Search -> setState { copy(query = intent.query) }
            is FeedersIntent.ToggleExpand -> setState {
                copy(expandedRouteCode = if (expandedRouteCode == intent.routeCode) null else intent.routeCode)
            }
            is FeedersIntent.SelectDirection -> sendEffect {
                FeedersEffect.NavigateToStops(intent.routeCode, intent.direction)
            }
            FeedersIntent.Retry -> loadFeeders(currentState.zone)
        }
    }

    private fun loadFeeders(zone: Zone) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching { getFeederRoutes(zone) }
                .onSuccess { routes -> setState { copy(isLoading = false, routes = routes) } }
                .onFailure { error ->
                    setState { copy(isLoading = false, errorMessage = error.message ?: "Error al cargar alimentadores") }
                }
        }
    }
}
