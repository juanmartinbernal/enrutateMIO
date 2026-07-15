package com.enrutatemio.data.recharge

import android.content.Context
import com.enrutatemio.core.common.geo.GeoUtils
import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.RechargePoint
import com.enrutatemio.domain.recharge.RechargeRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Puntos de recarga de la tarjeta MIO. Migra el flujo legacy que parseaba
 * `assets/puntos_recarga.json` de forma manual y duplicada en `PuntosRecargaActivity`
 * (lista, sin coordenadas) y `ShowMapActivity` (mapa, con coordenadas) — aquí se
 * unifica en un solo repositorio con caché en memoria.
 */
class RechargeRepositoryImpl(
    private val context: Context,
    private val moshi: Moshi,
) : RechargeRepository {

    private val mutex = Mutex()
    private var cache: List<RechargePoint>? = null

    override suspend fun getAllRechargePoints(): List<RechargePoint> = loadAll()

    override suspend fun searchRechargePoints(query: String): List<RechargePoint> {
        val all = loadAll()
        if (query.isBlank()) return all
        val normalized = query.trim().lowercase()
        return all.filter {
            it.name.lowercase().contains(normalized) || it.address.lowercase().contains(normalized)
        }
    }

    override suspend fun getNearbyRechargePoints(origin: Coordinates, radiusMeters: Double): List<RechargePoint> {
        val all = loadAll()
        return all.mapNotNull { point ->
            val distance = GeoUtils.distanceMeters(origin, point.coordinates)
            if (distance <= radiusMeters) point.copy(distanceMeters = distance) else null
        }.sortedBy { it.distanceMeters }
    }

    private suspend fun loadAll(): List<RechargePoint> = mutex.withLock {
        cache ?: run {
            val type = Types.newParameterizedType(List::class.java, RechargePointDto::class.java)
            val adapter = moshi.adapter<List<RechargePointDto>>(type)
            val json = context.assets.open(ASSET_NAME).use { input ->
                BufferedReader(InputStreamReader(input, Charsets.UTF_8)).readText()
            }
            val dtos = adapter.fromJson(json).orEmpty()
            dtos.map {
                RechargePoint(
                    id = it.numero.toString(),
                    name = it.nombre,
                    address = it.direccion,
                    coordinates = Coordinates(it.latitud, it.longitud),
                    type = it.tipo,
                )
            }.also { cache = it }
        }
    }

    private companion object {
        const val ASSET_NAME = "puntos_recarga.json"
    }
}
