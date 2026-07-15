package com.enrutatemio.domain.map

import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.DirectionsResult

/**
 * Cálculo de direcciones punto a punto. Reemplaza al backend propio legacy
 * (`tuyo.herokuapp.com`, hoy inactivo) por la Google Directions API.
 */
interface DirectionsRepository {
    suspend fun getWalkingDirections(origin: Coordinates, destination: Coordinates): DirectionsResult
    suspend fun getTransitDirections(origin: Coordinates, destination: Coordinates): DirectionsResult
}
