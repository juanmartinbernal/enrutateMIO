package com.enrutatemio.data.map

import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.DirectionsResult
import com.enrutatemio.core.model.LegType
import com.enrutatemio.core.model.RouteLeg
import com.enrutatemio.core.model.RouteStep
import com.enrutatemio.core.network.NetworkConfig
import com.enrutatemio.core.network.directions.DirectionsApi
import com.enrutatemio.core.network.directions.LegDto
import com.enrutatemio.domain.map.DirectionsRepository

class DirectionsRepositoryImpl(
    private val api: DirectionsApi,
    private val config: NetworkConfig,
) : DirectionsRepository {

    override suspend fun getWalkingDirections(origin: Coordinates, destination: Coordinates): DirectionsResult =
        fetch(origin, destination, mode = "walking")

    override suspend fun getTransitDirections(origin: Coordinates, destination: Coordinates): DirectionsResult =
        fetch(origin, destination, mode = "transit")

    private suspend fun fetch(origin: Coordinates, destination: Coordinates, mode: String): DirectionsResult {
        val response = api.getDirections(
            origin = "${origin.latitude},${origin.longitude}",
            destination = "${destination.latitude},${destination.longitude}",
            mode = mode,
            apiKey = config.googleMapsApiKey,
        )
        val leg = response.routes.firstOrNull()?.legs?.firstOrNull()
            ?: return emptyResult(origin, destination)

        return DirectionsResult(
            origin = origin,
            destination = destination,
            legs = leg.toDomainLegs(mode),
            durationText = leg.duration?.text.orEmpty(),
            durationSeconds = leg.duration?.value ?: 0,
            distanceText = leg.distance?.text.orEmpty(),
            distanceMeters = leg.distance?.value ?: 0,
        )
    }

    private fun LegDto.toDomainLegs(mode: String): List<RouteLeg> = steps.map { step ->
        val points = step.polyline?.points?.let(PolylineDecoder::decode).orEmpty()
        RouteLeg(
            type = if (mode == "walking" || step.travelMode == "WALKING") LegType.WALK else LegType.TRANSIT,
            walkDurationText = step.duration?.text,
            steps = points.map { coordinates ->
                RouteStep(
                    coordinates = coordinates,
                    instruction = step.htmlInstructions.replace(Regex("<[^>]*>"), ""),
                )
            },
        )
    }

    private fun emptyResult(origin: Coordinates, destination: Coordinates) = DirectionsResult(
        origin = origin,
        destination = destination,
        legs = emptyList(),
        durationText = "",
        durationSeconds = 0,
        distanceText = "",
        distanceMeters = 0,
    )
}
