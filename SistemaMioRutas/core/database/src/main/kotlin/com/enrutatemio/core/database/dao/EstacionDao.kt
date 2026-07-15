package com.enrutatemio.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enrutatemio.core.database.entity.EstacionEntity

@Dao
interface EstacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estaciones: List<EstacionEntity>)

    @Query("SELECT COUNT(*) FROM estaciones")
    suspend fun count(): Int

    @Query("SELECT * FROM estaciones WHERE tipo = '0' ORDER BY nombre ASC")
    suspend fun getAll(): List<EstacionEntity>

    @Query("SELECT * FROM estaciones WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    suspend fun search(query: String): List<EstacionEntity>

    @Query("SELECT * FROM estaciones WHERE nombre = :nombre LIMIT 1")
    suspend fun getByName(nombre: String): EstacionEntity?

    @Query("SELECT * FROM estaciones WHERE nombre LIKE '%' || :query || '%' LIMIT 1")
    suspend fun findByNameContains(query: String): EstacionEntity?
}
