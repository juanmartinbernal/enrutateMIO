package com.enrutatemio.domain.recharge

import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.RechargePoint

/**
 * Busca puntos de recarga cercanos a [origin]. Replica el comportamiento legacy de
 * `ActionListenerPuntosRecarga`: intenta primero con un radio de 500m, y si no hay
 * resultados amplía a 1000m.
 */
class GetNearbyRechargePointsUseCase(private val repository: RechargeRepository) {
    suspend operator fun invoke(
        origin: Coordinates,
        initialRadiusMeters: Double = 500.0,
        fallbackRadiusMeters: Double = 1000.0,
    ): List<RechargePoint> {
        val nearby = repository.getNearbyRechargePoints(origin, initialRadiusMeters)
        return nearby.ifEmpty { repository.getNearbyRechargePoints(origin, fallbackRadiusMeters) }
    }
}
