package com.enrutatemio.domain.news

import com.enrutatemio.core.model.NewsItem
import kotlinx.coroutines.flow.Flow

/** Fuente unificada de noticias (timeline de @METROCALI + backend propio de enrutateMIO). */
interface NewsRepository {

    /** Noticias cacheadas localmente (Room), ordenadas por fecha descendente. */
    fun observeNews(): Flow<List<NewsItem>>

    /** Descarga noticias nuevas de las fuentes remotas y actualiza la caché local. */
    suspend fun refreshNews(): Result<Unit>

    suspend fun markAsRead(id: String)
}
