package com.enrutatemio.feature.feeders.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone
import com.enrutatemio.core.ui.components.FullScreenEmpty
import com.enrutatemio.core.ui.components.FullScreenError
import com.enrutatemio.core.ui.components.FullScreenLoading
import com.enrutatemio.core.ui.components.RouteBadge
import com.enrutatemio.core.ui.components.SearchField
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedersRoute(
    onNavigateToStops: (routeCode: String, direction: RouteDirection) -> Unit,
    viewModel: FeedersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is FeedersEffect.NavigateToStops) {
                onNavigateToStops(effect.routeCode, effect.direction)
            }
        }
    }

    FeedersScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun FeedersScreen(state: FeedersState, onIntent: (FeedersIntent) -> Unit) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(Zone.entries) { zone ->
                    FilterChip(
                        selected = state.zone == zone,
                        onClick = { onIntent(FeedersIntent.SelectZone(zone)) },
                        label = { Text(zone.displayName) },
                    )
                }
            }

            SearchField(
                query = state.query,
                onQueryChange = { onIntent(FeedersIntent.Search(it)) },
                placeholder = "Buscar alimentador...",
                modifier = Modifier.fillMaxWidth(),
            )

            when {
                state.isLoading -> FullScreenLoading()
                state.errorMessage != null -> FullScreenError(
                    message = state.errorMessage,
                    onRetry = { onIntent(FeedersIntent.Retry) },
                )
                state.filteredRoutes.isEmpty() -> FullScreenEmpty("No hay alimentadores en esta zona")
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.filteredRoutes, key = { it.code }) { route ->
                        FeederCard(
                            route = route,
                            expanded = state.expandedRouteCode == route.code,
                            onClick = { onIntent(FeedersIntent.ToggleExpand(route.code)) },
                            onSelectDirection = { direction ->
                                onIntent(FeedersIntent.SelectDirection(route.code, direction))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeederCard(
    route: TransitRoute,
    expanded: Boolean,
    onClick: () -> Unit,
    onSelectDirection: (RouteDirection) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                RouteBadge(code = route.code, type = route.type)
            }
            route.trayecto?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
            }
            if (expanded) {
                Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(
                        "Norte → Sur" to RouteDirection.NORTE_SUR,
                        "Sur → Norte" to RouteDirection.SUR_NORTE,
                    ).forEach { (label, direction) ->
                        val summary = route.summaryFor(direction)
                        if (summary != null) {
                            Text(
                                text = "$label: $summary",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { onSelectDirection(direction) },
                            )
                        }
                    }
                }
            }
        }
    }
}
