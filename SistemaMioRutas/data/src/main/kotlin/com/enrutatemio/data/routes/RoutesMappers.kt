package com.enrutatemio.data.routes

import com.enrutatemio.core.database.entity.EstacionEntity
import com.enrutatemio.core.database.entity.RutaParadaEntity
import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.Station
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone

internal fun EstacionEntity.toDomain(): Station = Station(
    name = nombre,
    coordinates = Coordinates(latitud, longitud),
    routeCodes = listadoRutas,
    address = direccion.trim(),
)

private fun routeTypeFrom(dbValue: String): RouteType =
    RouteType.entries.firstOrNull { it.dbValue == dbValue } ?: RouteType.TRONCAL

private fun zoneFrom(dbValue: String?): Zone? =
    dbValue?.let { value -> Zone.entries.firstOrNull { it.dbValue == value } }

/** Mapea una única fila (un solo sentido) a [TransitRoute], usada en listados. */
internal fun RutaParadaEntity.toDomainSingleDirection(): TransitRoute {
    val stops = paradas
    return TransitRoute(
        code = ruta,
        type = routeTypeFrom(tipo),
        zone = zoneFrom(zona),
        trayecto = trayecto,
        horario = horario,
        northToSouthStops = if (sentido == "nortesur") stops else emptyList(),
        southToNorthStops = if (sentido == "surnorte") stops else emptyList(),
    )
}

/** Combina las 1-2 filas (una por sentido) de una misma ruta en un solo [TransitRoute]. */
internal fun List<RutaParadaEntity>.toDomainRoute(): TransitRoute? {
    val first = firstOrNull() ?: return null
    val northToSouth = firstOrNull { it.sentido == "nortesur" }
    val southToNorth = firstOrNull { it.sentido == "surnorte" }
    return TransitRoute(
        code = first.ruta,
        type = routeTypeFrom(first.tipo),
        zone = zoneFrom(first.zona),
        trayecto = northToSouth?.trayecto ?: southToNorth?.trayecto,
        horario = northToSouth?.horario ?: southToNorth?.horario,
        northToSouthStops = northToSouth?.paradas.orEmpty(),
        southToNorthStops = southToNorth?.paradas.orEmpty(),
    )
}


internal fun groupRoutesByCode(entities: List<RutaParadaEntity>): List<TransitRoute> =
    entities.groupBy { it.ruta }.values.mapNotNull { it.toDomainRoute() }
