package com.enrutatemio.data.feeders

import com.enrutatemio.core.database.dao.RutaParadaDao
import com.enrutatemio.core.model.TransitRoute
import com.enrutatemio.core.model.Zone
import com.enrutatemio.data.routes.toDomainSingleDirection
import com.enrutatemio.data.seed.DatabaseSeeder
import com.enrutatemio.domain.feeders.FeedersRepository

class FeedersRepositoryImpl(
    private val rutaParadaDao: RutaParadaDao,
    private val seeder: DatabaseSeeder,
) : FeedersRepository {

    override suspend fun getFeederRoutes(zone: Zone?): List<TransitRoute> {
        seeder.seedIfNeeded()
        val entities = if (zone == null || zone == Zone.TODAS) {
            rutaParadaDao.getAllFeeders()
        } else {
            rutaParadaDao.getFeedersByZone(zone.dbValue)
        }
        return entities.map { it.toDomainSingleDirection() }
    }
}
