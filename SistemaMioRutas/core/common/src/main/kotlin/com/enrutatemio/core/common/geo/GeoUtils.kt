package com.enrutatemio.core.common.geo

import com.enrutatemio.core.model.Coordinates
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Utilidades geográficas puras (sin dependencias de Android/Play Services),
 * migradas de `MapGoogle.distFrom()` legacy.
 */
object GeoUtils {

    private const val EARTH_RADIUS_METERS = 6_371_000.0

    /** Distancia Haversine en metros entre dos coordenadas. */
    fun distanceMeters(from: Coordinates, to: Coordinates): Double {
        val dLat = Math.toRadians(to.latitude - from.latitude)
        val dLng = Math.toRadians(to.longitude - from.longitude)
        val sinDLat = sin(dLat / 2)
        val sinDLng = sin(dLng / 2)
        val a = sinDLat.pow(2) + sinDLng.pow(2) *
            cos(Math.toRadians(from.latitude)) * cos(Math.toRadians(to.latitude))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }
}
