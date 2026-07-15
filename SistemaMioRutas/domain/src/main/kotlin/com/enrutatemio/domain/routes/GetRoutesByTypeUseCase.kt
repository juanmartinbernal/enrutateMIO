package com.enrutatemio.domain.routes

import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.TransitRoute

/** Lista las rutas (troncal/pretroncal/expreso) ordenadas por código. */
class GetRoutesByTypeUseCase(private val repository: RoutesRepository) {
    suspend operator fun invoke(type: RouteType): List<TransitRoute> =
        repository.getRoutesByType(type)
}
