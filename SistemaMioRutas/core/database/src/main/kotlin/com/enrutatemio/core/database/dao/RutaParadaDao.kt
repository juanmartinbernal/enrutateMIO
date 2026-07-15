package com.enrutatemio.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enrutatemio.core.database.entity.RutaParadaEntity

@Dao
interface RutaParadaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rutas: List<RutaParadaEntity>)

    @Query("SELECT COUNT(*) FROM ruta_paradas")
    suspend fun count(): Int

    @Query(
        "SELECT * FROM ruta_paradas WHERE tipo = :tipo AND estado = 'A' " +
            "AND sentido = 'nortesur' ORDER BY ruta ASC",
    )
    suspend fun getByType(tipo: String): List<RutaParadaEntity>

    @Query(
        "SELECT * FROM ruta_paradas WHERE tipo = 'alimentador' AND estado = 'A' " +
            "AND sentido = 'nortesur' ORDER BY ruta ASC",
    )
    suspend fun getAllFeeders(): List<RutaParadaEntity>

    @Query(
        "SELECT * FROM ruta_paradas WHERE tipo = 'alimentador' AND zona = :zona AND estado = 'A' " +
            "AND sentido = 'nortesur' ORDER BY ruta ASC",
    )
    suspend fun getFeedersByZone(zona: String): List<RutaParadaEntity>

    @Query("SELECT * FROM ruta_paradas WHERE ruta = :ruta AND sentido = :sentido LIMIT 1")
    suspend fun getRoute(ruta: String, sentido: String): RutaParadaEntity?

    @Query("SELECT * FROM ruta_paradas WHERE ruta = :ruta")
    suspend fun getRouteBothDirections(ruta: String): List<RutaParadaEntity>
}
