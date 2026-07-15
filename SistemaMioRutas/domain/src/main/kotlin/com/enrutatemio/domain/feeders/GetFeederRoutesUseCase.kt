package com.enrutatemio.domain.feeders

import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone

class GetFeederRoutesUseCase(private val repository: FeedersRepository) {
    suspend operator fun invoke(zone: Zone? = null): List<TransitRoute> =
        repository.getFeederRoutes(zone)
}
