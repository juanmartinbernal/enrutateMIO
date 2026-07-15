package com.enrutatemio.data.seed

import android.content.Context
import com.enrutatemio.core.database.dao.EstacionDao
import com.enrutatemio.core.database.dao.RutaParadaDao
import com.enrutatemio.core.database.entity.EstacionEntity
import com.enrutatemio.core.database.entity.RutaParadaEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Puebla la base de datos Room con el dataset real exportado del `test.db` legacy
 * (58 estaciones, 241 filas ruta_paradas) la primera vez que se ejecuta la app.
 * Reemplaza a `AdminSQLiteOpenHelper.createDatabase()` (que copiaba el .db byte a byte).
 */
class DatabaseSeeder(
    private val context: Context,
    private val moshi: Moshi,
    private val estacionDao: EstacionDao,
    private val rutaParadaDao: RutaParadaDao,
) {
    private val mutex = Mutex()

    suspend fun seedIfNeeded() {
        mutex.withLock {
            if (estacionDao.count() == 0) {
                estacionDao.insertAll(readEstaciones())
            }
            if (rutaParadaDao.count() == 0) {
                rutaParadaDao.insertAll(readRutas())
            }
        }
    }

    private fun readEstaciones(): List<EstacionEntity> {
        val type = Types.newParameterizedType(List::class.java, SeedEstacionDto::class.java)
        val adapter = moshi.adapter<List<SeedEstacionDto>>(type)
        val json = readAsset(ASSET_ESTACIONES)
        val dtos = adapter.fromJson(json).orEmpty()
        return dtos.filter { it.latitud != null && it.longitud != null }.map {
            EstacionEntity(
                nombre = it.nombre,
                latitud = it.latitud!!,
                longitud = it.longitud!!,
                listadoRutas = it.listadoRutas,
                tipo = it.tipo,
                direccion = it.direccion,
            )
        }
    }

    private fun readRutas(): List<RutaParadaEntity> {
        val type = Types.newParameterizedType(List::class.java, SeedRutaParadaDto::class.java)
        val adapter = moshi.adapter<List<SeedRutaParadaDto>>(type)
        val json = readAsset(ASSET_RUTAS)
        val dtos = adapter.fromJson(json).orEmpty()
        return dtos.map {
            RutaParadaEntity(
                ruta = it.ruta,
                sentido = it.sentido,
                paradas = it.paradas,
                tipo = it.tipo,
                zona = it.zona,
                estado = it.estado,
                trayecto = it.trayecto,
                horario = it.horario,
            )
        }
    }

    private fun readAsset(fileName: String): String =
        context.assets.open(fileName).use { input ->
            BufferedReader(InputStreamReader(input, Charsets.UTF_8)).readText()
        }

    private companion object {
        const val ASSET_ESTACIONES = "seed_estaciones.json"
        const val ASSET_RUTAS = "seed_ruta_paradas.json"
    }
}
