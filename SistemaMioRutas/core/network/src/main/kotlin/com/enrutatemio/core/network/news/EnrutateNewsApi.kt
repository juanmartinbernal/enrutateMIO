package com.enrutatemio.core.network.news

import com.squareup.moshi.JsonClass
import retrofit2.http.GET

/**
 * Backend propio de EnrútateMIO (histórico: `enrutatemio.com/enrutate/noticias.php`).
 * Se conserva como interfaz reemplazable; si el dominio original ya no está activo,
 * basta con apuntar [com.enrutatemio.core.network.NetworkConfig.enrutateBackendBaseUrl]
 * a un backend propio actualizado.
 */
interface EnrutateNewsApi {
    @GET("enrutate/noticias.php")
    suspend fun getNews(): List<EnrutateNewsDto>
}

@JsonClass(generateAdapter = true)
data class EnrutateNewsDto(
    val noticia: String = "",
    val fecha: String = "",
    val estado: String = "N",
)
