package com.enrutatemio.domain.recharge

import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.RechargePoint

/** Acceso a los puntos de recarga de la tarjeta MIO (dataset local `puntos_recarga.json`). */
interface RechargeRepository {

    suspend fun getAllRechargePoints(): List<RechargePoint>

    suspend fun searchRechargePoints(query: String): List<RechargePoint>

    /**
     * Puntos dentro de [radiusMeters] de [origin], ordenados por cercanía.
     * Replica el fallback legacy (500m, luego 1000m si no hay resultados) en el use case.
     */
    suspend fun getNearbyRechargePoints(origin: Coordinates, radiusMeters: Double): List<RechargePoint>
}
