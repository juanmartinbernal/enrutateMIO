package com.enrutatemio.data.news

import com.enrutatemio.core.database.entity.NoticiaEntity
import com.enrutatemio.core.model.NewsItem
import com.enrutatemio.core.model.NewsSource

internal fun NoticiaEntity.toDomain(): NewsItem = NewsItem(
    id = id,
    text = texto,
    publishedAt = publicadaEnMillis,
    source = NewsSource.valueOf(fuente),
    isRead = leida,
    authorName = autorNombre,
    authorAvatarUrl = autorAvatarUrl,
    isUrgent = esUrgente,
)
