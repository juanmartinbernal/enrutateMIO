package com.enrutatemio.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Estación troncal del sistema MIO (migrada de la tabla legacy `estaciones`). */
@Entity(tableName = "estaciones")
data class EstacionEntity(
    @PrimaryKey val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val listadoRutas: List<String>,
    val tipo: String,
    val direccion: String,
)
