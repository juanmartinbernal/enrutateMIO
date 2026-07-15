package com.enrutatemio.domain.feeders

import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone

/** Acceso a las rutas alimentadoras, filtrables por zona geográfica. */
interface FeedersRepository {
    suspend fun getFeederRoutes(zone: Zone? = null): List<TransitRoute>
}
