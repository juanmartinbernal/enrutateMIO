package com.enrutatemio.data.routes

import com.enrutatemio.core.database.dao.EstacionDao
import com.enrutatemio.core.database.dao.RutaParadaDao
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.core.model.RouteType
import com.enrutatemio.core.model.Station
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.data.seed.DatabaseSeeder
import com.enrutatemio.domain.routes.RoutesRepository
import com.enrutatemio.domain.routes.StationKind

class RoutesRepositoryImpl(
    private val estacionDao: EstacionDao,
    private val rutaParadaDao: RutaParadaDao,
    private val seeder: DatabaseSeeder,
) : RoutesRepository {

    override suspend fun getRoutesByType(type: RouteType): List<TransitRoute> {
        seeder.seedIfNeeded()
        return rutaParadaDao.getByType(type.dbValue).map { it.toDomainSingleDirection() }
    }

    override suspend fun getRoute(code: String): TransitRoute? {
        seeder.seedIfNeeded()
        return groupRoutesByCode(rutaParadaDao.getRouteBothDirections(code)).firstOrNull()
    }

    override suspend fun getAllStations(): List<Station> {
        seeder.seedIfNeeded()
        return estacionDao.getAll().map { it.toDomain() }
    }

    override suspend fun searchStations(query: String): List<Station> {
        seeder.seedIfNeeded()
        return estacionDao.search(query).map { it.toDomain() }
    }

    override suspend fun getRouteCodesForStation(stationName: String): List<String> {
        seeder.seedIfNeeded()
        val estacion = estacionDao.findByNameContains(stationName.removePrefix("ESTACIÓN ").trim())
        return estacion?.listadoRutas.orEmpty()
    }

    override suspend fun getStationKind(stationName: String): StationKind? {
        seeder.seedIfNeeded()
        val estacion = estacionDao.findByNameContains(stationName)
        return when (estacion?.tipo) {
            "0" -> StationKind.ESTACION
            "1" -> StationKind.PARADA
            "2" -> StationKind.ALIMENTADOR
            else -> null
        }
    }

    override suspend fun getStopsInDirection(routeCode: String, direction: RouteDirection): List<String> {
        seeder.seedIfNeeded()
        return rutaParadaDao.getRoute(routeCode, direction.dbValue)?.paradas.orEmpty()
    }
}
