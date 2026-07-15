package com.enrutatemio.core.network.directions

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Google Directions API. Reemplaza al backend propio legacy (`tuyo.herokuapp.com`,
 * hoy inactivo) para calcular rutas punto a punto.
 */
interface DirectionsApi {

    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("language") language: String = "es",
        @Query("key") apiKey: String,
    ): DirectionsResponseDto
}
