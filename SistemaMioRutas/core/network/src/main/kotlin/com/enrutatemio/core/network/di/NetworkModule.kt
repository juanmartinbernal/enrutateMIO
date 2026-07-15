package com.enrutatemio.core.network.di

import com.enrutatemio.core.network.NetworkConfig
import com.enrutatemio.core.network.directions.DirectionsApi
import com.enrutatemio.core.network.news.EnrutateNewsApi
import com.enrutatemio.core.network.twitter.TwitterApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/"
private const val TWITTER_BASE_URL = "https://api.twitter.com/"

private const val QUALIFIER_DIRECTIONS_RETROFIT = "directions_retrofit"
private const val QUALIFIER_TWITTER_RETROFIT = "twitter_retrofit"
private const val QUALIFIER_BACKEND_RETROFIT = "backend_retrofit"

val networkModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single<Interceptor>(named("twitterAuth")) {
        val config = get<NetworkConfig>()
        Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${config.twitterBearerToken}")
                .build()
            chain.proceed(request)
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named(QUALIFIER_DIRECTIONS_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(GOOGLE_MAPS_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single(named(QUALIFIER_TWITTER_RETROFIT)) {
        val client = get<OkHttpClient>().newBuilder()
            .addInterceptor(get<Interceptor>(named("twitterAuth")))
            .build()
        Retrofit.Builder()
            .baseUrl(TWITTER_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single(named(QUALIFIER_BACKEND_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(get<NetworkConfig>().enrutateBackendBaseUrl)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>(named(QUALIFIER_DIRECTIONS_RETROFIT)).create(DirectionsApi::class.java) }
    single { get<Retrofit>(named(QUALIFIER_TWITTER_RETROFIT)).create(TwitterApi::class.java) }
    single { get<Retrofit>(named(QUALIFIER_BACKEND_RETROFIT)).create(EnrutateNewsApi::class.java) }
}
