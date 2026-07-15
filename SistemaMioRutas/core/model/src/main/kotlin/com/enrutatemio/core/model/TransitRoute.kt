package com.enrutatemio.core.model

/**
 * Estación troncal del sistema MIO (tabla legacy `estaciones`).
 * Corresponde a las estaciones "grandes" del sistema (no a paradas menores de buses alimentadores).
 */
data class Station(
    val name: String,
    val coordinates: Coordinates,
    /** Códigos de ruta que pasan por esta estación, ej. ["E21", "E31", "T31"]. */
    val routeCodes: List<String>,
    val address: String,
)

/**
 * Una parada individual dentro del recorrido ordenado de una ruta.
 * En el sistema legacy es solo texto libre (nombre de estación o dirección),
 * sin coordenadas propias garantizadas.
 */
data class RouteStop(
    val name: String,
    val isFirst: Boolean = false,
    val isLast: Boolean = false,
    val isHighlighted: Boolean = false,
)

/**
 * Ruta de transporte del sistema MIO (tabla legacy `ruta_paradas`).
 * Cada ruta tiene 1-2 registros, uno por sentido (Norte-Sur / Sur-Norte).
 */
data class TransitRoute(
    val code: String,
    val type: RouteType,
    val zone: Zone? = null,
    val trayecto: String? = null,
    val horario: String? = null,
    val northToSouthStops: List<String> = emptyList(),
    val southToNorthStops: List<String> = emptyList(),
) {
    fun stopsFor(direction: RouteDirection): List<String> = when (direction) {
        RouteDirection.NORTE_SUR -> northToSouthStops
        RouteDirection.SUR_NORTE -> southToNorthStops
    }

    /** Texto resumen "Desde: X a Y" usado en las pantallas legacy. */
    fun summaryFor(direction: RouteDirection): String? {
        val stops = stopsFor(direction)
        if (stops.isEmpty()) return null
        return "${stops.first()} a ${stops.last()}"
    }
}
