package com.enrutatemio.domain.map

import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.DirectionsResult

class GetWalkingDirectionsUseCase(private val repository: DirectionsRepository) {
    suspend operator fun invoke(origin: Coordinates, destination: Coordinates): DirectionsResult =
        repository.getWalkingDirections(origin, destination)
}

class GetTransitDirectionsUseCase(private val repository: DirectionsRepository) {
    suspend operator fun invoke(origin: Coordinates, destination: Coordinates): DirectionsResult =
        repository.getTransitDirections(origin, destination)
}
