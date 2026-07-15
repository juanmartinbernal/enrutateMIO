package com.enrutatemio.core.model

/** Punto de recarga de la tarjeta MIO (dataset local `puntos_recarga.json`). */
data class RechargePoint(
    val id: String,
    val name: String,
    val address: String,
    val coordinates: Coordinates,
    val type: String,
    /** Distancia en metros a un punto de referencia, calculada bajo demanda. */
    val distanceMeters: Double? = null,
)
