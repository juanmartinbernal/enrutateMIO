package com.enrutatemio.feature.routes.stops

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteStop
import com.enrutatemio.core.ui.components.FullScreenEmpty
import com.enrutatemio.core.ui.components.FullScreenError
import com.enrutatemio.core.ui.components.FullScreenLoading
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RouteStopsRoute(
    routeCode: String,
    direction: RouteDirection,
    onBack: () -> Unit,
    viewModel: RouteStopsViewModel = koinViewModel { parametersOf(routeCode, direction) },
) {
    val state by viewModel.state.collectAsState()
    RouteStopsScreen(state = state, onBack = onBack, onRetry = { viewModel.onIntent(RouteStopsIntent.Retry) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteStopsScreen(
    state: RouteStopsState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ruta ${state.routeCode}") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading -> FullScreenLoading(modifier = Modifier.padding(padding))
            state.errorMessage != null -> FullScreenError(
                message = state.errorMessage,
                onRetry = onRetry,
                modifier = Modifier.padding(padding),
            )
            state.stops.isEmpty() -> FullScreenEmpty("Sin paradas registradas", modifier = Modifier.padding(padding))
            else -> LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.stops) { stop -> StopRow(stop) }
            }
        }
    }
}

@Composable
private fun StopRow(stop: RouteStop) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (icon, tint) = when {
            stop.isFirst -> Icons.Default.PlayArrow to MaterialTheme.colorScheme.primary
            stop.isLast -> Icons.Default.Flag to MaterialTheme.colorScheme.error
            stop.isHighlighted -> Icons.Default.Star to MaterialTheme.colorScheme.tertiary
            else -> Icons.Default.ArrowDownward to Color.Gray
        }
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stop.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (stop.isHighlighted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
        )
    }
}
