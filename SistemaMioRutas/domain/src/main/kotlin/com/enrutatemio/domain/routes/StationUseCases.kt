package com.enrutatemio.domain.routes

import com.enrutatemio.core.model.Station

class SearchStationsUseCase(private val repository: RoutesRepository) {
    suspend operator fun invoke(query: String): List<Station> =
        if (query.isBlank()) repository.getAllStations() else repository.searchStations(query)
}

class GetAllStationsUseCase(private val repository: RoutesRepository) {
    suspend operator fun invoke(): List<Station> = repository.getAllStations()
}

class GetStationDetailsUseCase(private val repository: RoutesRepository) {
    suspend operator fun invoke(stationName: String): StationDetails {
        val kind = repository.getStationKind(stationName)
        val codes = repository.getRouteCodesForStation(stationName)
        return StationDetails(kind = kind, routeCodes = codes)
    }
}

data class StationDetails(
    val kind: StationKind?,
    val routeCodes: List<String>,
)
