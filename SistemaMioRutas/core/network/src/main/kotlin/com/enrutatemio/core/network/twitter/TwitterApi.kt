package com.enrutatemio.core.network.twitter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Twitter API v2 (App-only auth, Bearer token) para leer el timeline público de @METROCALI.
 * Reemplaza el flujo OAuth 1.0a de Twitter4j usado en la app legacy, ya que la app solo
 * necesita LEER el timeline, no publicar en nombre del usuario.
 */
interface TwitterApi {

    @GET("2/users/by/username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): TwitterUserResponseDto

    @GET("2/users/{id}/tweets")
    suspend fun getUserTweets(
        @Path("id") userId: String,
        @Query("max_results") maxResults: Int = 20,
        @Query("tweet.fields") tweetFields: String = "created_at",
        @Query("exclude") exclude: String = "retweets,replies",
    ): TwitterTweetsResponseDto
}

@JsonClass(generateAdapter = true)
data class TwitterUserResponseDto(
    val data: TwitterUserDto? = null,
)

@JsonClass(generateAdapter = true)
data class TwitterUserDto(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    @Json(name = "profile_image_url") val profileImageUrl: String? = null,
)

@JsonClass(generateAdapter = true)
data class TwitterTweetsResponseDto(
    val data: List<TweetDto> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class TweetDto(
    val id: String = "",
    val text: String = "",
    @Json(name = "created_at") val createdAt: String? = null,
)
