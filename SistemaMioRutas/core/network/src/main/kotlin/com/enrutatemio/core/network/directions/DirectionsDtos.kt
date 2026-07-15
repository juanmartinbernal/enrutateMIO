package com.enrutatemio.core.network.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DirectionsResponseDto(
    val routes: List<RouteDto> = emptyList(),
    val status: String = "",
)

@JsonClass(generateAdapter = true)
data class RouteDto(
    val legs: List<LegDto> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class LegDto(
    val steps: List<StepDto> = emptyList(),
    val duration: TextValueDto? = null,
    val distance: TextValueDto? = null,
    @Json(name = "start_address") val startAddress: String? = null,
    @Json(name = "end_address") val endAddress: String? = null,
)

@JsonClass(generateAdapter = true)
data class StepDto(
    @Json(name = "html_instructions") val htmlInstructions: String = "",
    val polyline: PolylineDto? = null,
    val duration: TextValueDto? = null,
    val distance: TextValueDto? = null,
    @Json(name = "travel_mode") val travelMode: String? = null,
)

@JsonClass(generateAdapter = true)
data class PolylineDto(
    val points: String = "",
)

@JsonClass(generateAdapter = true)
data class TextValueDto(
    val text: String = "",
    val value: Int = 0,
)
