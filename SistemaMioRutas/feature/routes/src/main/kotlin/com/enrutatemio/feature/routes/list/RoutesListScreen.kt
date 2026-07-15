package com.enrutatemio.feature.routes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.ui.components.FullScreenEmpty
import com.enrutatemio.core.ui.components.FullScreenError
import com.enrutatemio.core.ui.components.FullScreenLoading
import com.enrutatemio.core.ui.components.RouteBadge
import com.enrutatemio.core.ui.components.SearchField
import org.koin.androidx.compose.koinViewModel

private val routeTypeLabels = mapOf(
    RouteType.TRONCAL to "Troncal",
    RouteType.PRETRONCAL to "Pretroncal",
    RouteType.EXPRESO to "Expreso",
)

@Composable
fun RoutesListRoute(
    onNavigateToStops: (routeCode: String, direction: RouteDirection) -> Unit,
    viewModel: RoutesListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect: UiEffect ->
            if (effect is RoutesListEffect.NavigateToStops) {
                onNavigateToStops(effect.routeCode, effect.direction)
            }
        }
    }

    RoutesListScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun RoutesListScreen(
    state: RoutesListState,
    onIntent: (RoutesListIntent) -> Unit,
) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = routeTypeLabels.keys.indexOf(state.selectedType).coerceAtLeast(0),
            ) {
                routeTypeLabels.forEach { (type, label) ->
                    Tab(
                        selected = state.selectedType == type,
                        onClick = { onIntent(RoutesListIntent.SelectType(type)) },
                        text = { Text(label) },
                    )
                }
            }

            SearchField(
                query = state.query,
                onQueryChange = { onIntent(RoutesListIntent.Search(it)) },
                placeholder = "Buscar ruta...",
                modifier = Modifier.fillMaxWidth(),
            )

            when {
                state.isLoading -> FullScreenLoading()
                state.errorMessage != null -> FullScreenError(
                    message = state.errorMessage,
                    onRetry = { onIntent(RoutesListIntent.Retry) },
                )
                state.filteredRoutes.isEmpty() -> FullScreenEmpty("No se encontraron rutas")
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.filteredRoutes, key = { it.code }) { route ->
                        RouteCard(
                            route = route,
                            expanded = state.expandedRouteCode == route.code,
                            onClick = { onIntent(RoutesListIntent.ToggleExpand(route.code)) },
                            onSelectDirection = { direction ->
                                onIntent(RoutesListIntent.SelectDirection(route.code, direction))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteCard(
    route: TransitRoute,
    expanded: Boolean,
    onClick: () -> Unit,
    onSelectDirection: (RouteDirection) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                RouteBadge(code = route.code, type = route.type)
                route.trayecto?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                route.horario?.let {
                    Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    DirectionRow("Norte → Sur", RouteDirection.NORTE_SUR, route, onSelectDirection)
                    DirectionRow("Sur → Norte", RouteDirection.SUR_NORTE, route, onSelectDirection)
                }
            }
        }
    }
}

@Composable
private fun DirectionRow(
    label: String,
    direction: RouteDirection,
    route: TransitRoute,
    onSelectDirection: (RouteDirection) -> Unit,
) {
    val summary = route.summaryFor(direction) ?: return
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
