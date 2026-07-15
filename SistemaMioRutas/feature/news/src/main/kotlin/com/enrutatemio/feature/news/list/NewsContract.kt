package com.enrutatemio.feature.news.list

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.NewsItem

data class NewsListState(
    val news: List<NewsItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : UiState

sealed interface NewsListIntent : UiIntent {
    data object Refresh : NewsListIntent
    data class MarkAsRead(val id: String) : NewsListIntent
}

sealed interface NewsListEffect : UiEffect
