package com.enrutatemio.core.model

/** Tipo de tramo de una dirección/ruta calculada punto a punto. */
enum class LegType {
    WALK,
    TRANSIT,
}

/** Un paso dentro de un tramo de viaje (equivalente al `Point` legacy). */
data class RouteStep(
    val coordinates: Coordinates,
    val instruction: String,
)

/** Tramo de un viaje (caminar o ir en bus), equivalente a `Section` legacy. */
data class RouteLeg(
    val type: LegType,
    val busLine: String? = null,
    val walkDurationText: String? = null,
    val steps: List<RouteStep> = emptyList(),
) {
    val polylinePoints: List<Coordinates> get() = steps.map { it.coordinates }
}

/** Resultado completo de direcciones punto a punto (Google Directions API). */
data class DirectionsResult(
    val origin: Coordinates,
    val destination: Coordinates,
    val legs: List<RouteLeg>,
    val durationText: String,
    val durationSeconds: Int,
    val distanceText: String,
    val distanceMeters: Int,
)
