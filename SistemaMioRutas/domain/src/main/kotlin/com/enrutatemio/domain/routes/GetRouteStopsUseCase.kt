package com.enrutatemio.domain.routes

import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteStop

/**
 * Arma la lista ordenada de paradas de una ruta en un sentido dado, resaltando
 * inicio/fin y (opcionalmente) una parada de referencia buscada por el usuario.
 */
class GetRouteStopsUseCase(private val repository: RoutesRepository) {

    suspend operator fun invoke(
        routeCode: String,
        direction: RouteDirection,
        highlightStation: String? = null,
    ): List<RouteStop> {
        val stops = repository.getStopsInDirection(routeCode, direction)
        return stops.mapIndexed { index, name ->
            RouteStop(
                name = name,
                isFirst = index == 0,
                isLast = index == stops.lastIndex,
                isHighlighted = highlightStation != null &&
                    name.contains(highlightStation, ignoreCase = true),
            )
        }
    }
}
