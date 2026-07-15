package com.enrutatemio.core.model

/** Origen de una noticia mostrada en el feed de la app. */
enum class NewsSource {
    METROCALI_TWITTER,
    ENRUTATEMIO_BACKEND,
    USER_TWEET,
}

/** Noticia u actualización relevante para el usuario (reemplaza al DTO legacy `Noticia`). */
data class NewsItem(
    val id: String,
    val text: String,
    val publishedAt: Long,
    val source: NewsSource,
    val isRead: Boolean = false,
    val authorName: String? = null,
    val authorAvatarUrl: String? = null,
    val isUrgent: Boolean = false,
)
