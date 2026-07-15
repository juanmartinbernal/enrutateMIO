package com.enrutatemio.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enrutatemio.core.database.entity.NoticiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticiaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noticias: List<NoticiaEntity>)

    @Query("SELECT * FROM noticias ORDER BY publicadaEnMillis DESC")
    fun observeAll(): Flow<List<NoticiaEntity>>

    @Query("UPDATE noticias SET leida = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("SELECT COUNT(*) FROM noticias WHERE id = :id")
    suspend fun exists(id: String): Int
}
