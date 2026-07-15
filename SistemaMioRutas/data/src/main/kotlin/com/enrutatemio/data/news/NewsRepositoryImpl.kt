package com.enrutatemio.data.news

import com.enrutatemio.core.database.dao.NoticiaDao
import com.enrutatemio.core.database.entity.NoticiaEntity
import com.enrutatemio.core.model.NewsItem
import com.enrutatemio.core.model.NewsSource
import com.enrutatemio.core.network.news.EnrutateNewsApi
import com.enrutatemio.core.network.twitter.TwitterApi
import com.enrutatemio.domain.news.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Fuente unificada de noticias: timeline de @METROCALI (Twitter API v2, Bearer token
 * app-only, reemplazando el OAuth 1.0a de Twitter4j legacy) + backend propio PHP.
 * Ambas fuentes se combinan y cachean en Room; si una falla, la otra sigue funcionando.
 */
class NewsRepositoryImpl(
    private val twitterApi: TwitterApi,
    private val enrutateNewsApi: EnrutateNewsApi,
    private val noticiaDao: NoticiaDao,
) : NewsRepository {

    private var cachedTwitterUserId: String? = null

    override fun observeNews(): Flow<List<NewsItem>> =
        noticiaDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshNews(): Result<Unit> {
        var anySucceeded = false
        var lastError: Throwable? = null

        runCatching { fetchTwitterNews() }
            .onSuccess { noticiaDao.insertAll(it); anySucceeded = true }
            .onFailure { lastError = it }

        runCatching { fetchBackendNews() }
            .onSuccess { noticiaDao.insertAll(it); anySucceeded = true }
            .onFailure { lastError = it }

        return if (anySucceeded) Result.success(Unit) else Result.failure(lastError ?: Exception("Sin conexión"))
    }

    override suspend fun markAsRead(id: String) = noticiaDao.markAsRead(id)

    private suspend fun fetchTwitterNews(): List<NoticiaEntity> {
        val userId = cachedTwitterUserId ?: twitterApi.getUserByUsername(METROCALI_USERNAME)
            .data?.id?.also { cachedTwitterUserId = it }
            ?: error("No se pudo resolver el usuario de Twitter")

        val tweets = twitterApi.getUserTweets(userId).data
        return tweets
            .filter { it.text.contains(URGENT_HASHTAG, ignoreCase = true) }
            .map { tweet ->
                NoticiaEntity(
                    id = "tw_${tweet.id}",
                    texto = tweet.text,
                    publicadaEnMillis = parseIsoDate(tweet.createdAt),
                    fuente = NewsSource.METROCALI_TWITTER.name,
                    leida = false,
                    esUrgente = true,
                    autorNombre = "Metrocali",
                    autorAvatarUrl = null,
                )
            }
    }

    private suspend fun fetchBackendNews(): List<NoticiaEntity> =
        enrutateNewsApi.getNews().map { dto ->
            NoticiaEntity(
                id = "be_${dto.noticia.hashCode()}_${dto.fecha.hashCode()}",
                texto = dto.noticia,
                publicadaEnMillis = parseLegacyDate(dto.fecha),
                fuente = NewsSource.ENRUTATEMIO_BACKEND.name,
                leida = dto.estado == "R",
                esUrgente = false,
                autorNombre = "Enrútate MIO",
                autorAvatarUrl = null,
            )
        }

    private fun parseIsoDate(value: String?): Long = runCatching {
        Instant.from(DateTimeFormatter.ISO_INSTANT.parse(value)).toEpochMilli()
    }.getOrDefault(System.currentTimeMillis())

    private fun parseLegacyDate(value: String): Long = runCatching {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(value)?.time
    }.getOrNull() ?: System.currentTimeMillis()

    private companion object {
        const val METROCALI_USERNAME = "METROCALI"
        const val URGENT_HASHTAG = "#ATENCIÓN"
    }
}
