package com.enrutatemio.domain.routes

import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.Station
import com.enrutatemio.core.model.TransitRoute

/** Tipo de parada/estación (equivalente a `Consultas.tipoParada` legacy). */
enum class StationKind { ESTACION, PARADA, ALIMENTADOR }

/**
 * Acceso a la información local de rutas y estaciones del sistema MIO
 * (respaldado por las tablas legacy `estaciones` / `ruta_paradas`, migradas a Room).
 */
interface RoutesRepository {

    suspend fun getRoutesByType(type: RouteType): List<TransitRoute>

    suspend fun getRoute(code: String): TransitRoute?

    suspend fun getAllStations(): List<Station>

    suspend fun searchStations(query: String): List<Station>

    /** Códigos de ruta que pasan por una estación dada, ej. ["E21", "T31"]. */
    suspend fun getRouteCodesForStation(stationName: String): List<String>

    suspend fun getStationKind(stationName: String): StationKind?

    suspend fun getStopsInDirection(routeCode: String, direction: RouteDirection): List<String>
}
