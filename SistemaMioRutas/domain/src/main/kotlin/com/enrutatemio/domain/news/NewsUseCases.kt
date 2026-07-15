package com.enrutatemio.domain.news

import com.enrutatemio.core.model.NewsItem
import kotlinx.coroutines.flow.Flow

class ObserveNewsUseCase(private val repository: NewsRepository) {
    operator fun invoke(): Flow<List<NewsItem>> = repository.observeNews()
}

class RefreshNewsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshNews()
}

class MarkNewsAsReadUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(id: String) = repository.markAsRead(id)
}
