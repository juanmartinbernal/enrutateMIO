package com.enrutatemio.feature.news.list

import androidx.lifecycle.viewModelScope
import com.enrutatemio.core.common.mvi.BaseMviViewModel
import com.enrutatemio.domain.news.MarkNewsAsReadUseCase
import com.enrutatemio.domain.news.ObserveNewsUseCase
import com.enrutatemio.domain.news.RefreshNewsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsListViewModel(
    observeNews: ObserveNewsUseCase,
    private val refreshNews: RefreshNewsUseCase,
    private val markAsRead: MarkNewsAsReadUseCase,
) : BaseMviViewModel<NewsListState, NewsListIntent, NewsListEffect>(NewsListState()) {

    init {
        observeNews()
            .onEach { news -> setState { copy(news = news, isLoading = false) } }
            .launchIn(viewModelScope)

        refresh()
    }

    override fun onIntent(intent: NewsListIntent) {
        when (intent) {
            NewsListIntent.Refresh -> refresh()
            is NewsListIntent.MarkAsRead -> viewModelScope.launch { markAsRead(intent.id) }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true, errorMessage = null) }
            refreshNews()
                .onFailure { error -> setState { copy(errorMessage = error.message) } }
            setState { copy(isRefreshing = false, isLoading = false) }
        }
    }
}
