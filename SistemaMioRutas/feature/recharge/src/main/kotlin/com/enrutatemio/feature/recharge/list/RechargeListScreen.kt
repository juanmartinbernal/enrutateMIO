package com.enrutatemio.feature.recharge.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.model.RechargePoint
import com.enrutatemio.core.ui.components.FullScreenEmpty
import com.enrutatemio.core.ui.components.FullScreenError
import com.enrutatemio.core.ui.components.FullScreenLoading
import com.enrutatemio.core.ui.components.SearchField
import org.koin.androidx.compose.koinViewModel

@Composable
fun RechargeListRoute(viewModel: RechargeListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    RechargeListScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun RechargeListScreen(state: RechargeListState, onIntent: (RechargeListIntent) -> Unit) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SearchField(
                query = state.query,
                onQueryChange = { onIntent(RechargeListIntent.Search(it)) },
                placeholder = "Buscar punto de recarga...",
                modifier = Modifier.fillMaxWidth(),
            )

            when {
                state.isLoading -> FullScreenLoading()
                state.errorMessage != null -> FullScreenError(
                    message = state.errorMessage,
                    onRetry = { onIntent(RechargeListIntent.Retry) },
                )
                state.points.isEmpty() -> FullScreenEmpty("No se encontraron puntos de recarga")
                else -> LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(state.points, key = { it.id }) { point ->
                        RechargePointRow(point)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun RechargePointRow(point: RechargePoint) {
    ListItem(
        headlineContent = { Text(point.name, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = { Text(point.address, style = MaterialTheme.typography.bodyMedium) },
    )
}
