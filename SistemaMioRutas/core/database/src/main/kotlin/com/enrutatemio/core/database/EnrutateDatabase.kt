package com.enrutatemio.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.enrutatemio.core.database.dao.EstacionDao
import com.enrutatemio.core.database.dao.NoticiaDao
import com.enrutatemio.core.database.dao.RutaParadaDao
import com.enrutatemio.core.database.entity.EstacionEntity
import com.enrutatemio.core.database.entity.NoticiaEntity
import com.enrutatemio.core.database.entity.RutaParadaEntity

@Database(
    entities = [EstacionEntity::class, RutaParadaEntity::class, NoticiaEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(StringListConverter::class)
abstract class EnrutateDatabase : RoomDatabase() {
    abstract fun estacionDao(): EstacionDao
    abstract fun rutaParadaDao(): RutaParadaDao
    abstract fun noticiaDao(): NoticiaDao

    companion object {
        const val DATABASE_NAME = "enrutatemio.db"
    }
}
