package com.enrutatemio.feature.news.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.model.NewsItem
import com.enrutatemio.core.ui.components.FullScreenEmpty
import com.enrutatemio.core.ui.components.FullScreenLoading
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewsListRoute(viewModel: NewsListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    NewsListScreen(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListScreen(state: NewsListState, onIntent: (NewsListIntent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Noticias") },
                actions = {
                    IconButton(onClick = { onIntent(NewsListIntent.Refresh) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (state.isRefreshing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            when {
                state.isLoading -> FullScreenLoading()
                state.news.isEmpty() -> FullScreenEmpty("No hay noticias por el momento")
                else -> LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(state.news, key = { it.id }) { news ->
                        NewsCard(news, onClick = { onIntent(NewsListIntent.MarkAsRead(news.id)) })
                    }
                }
            }
        }
    }
}


@Composable
private fun NewsCard(news: NewsItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = if (news.isRead) 0.dp else 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                if (news.isUrgent) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Urgente",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = news.authorName ?: "Enrútate MIO",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (!news.isRead) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = "No leída",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(8.dp),
                    )
                }
            }
            Text(
                text = news.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = formatDate(news.publishedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "CO")).format(Date(millis))
