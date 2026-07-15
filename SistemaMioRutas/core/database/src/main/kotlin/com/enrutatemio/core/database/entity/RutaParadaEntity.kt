package com.enrutatemio.core.database.entity

import androidx.room.Entity

/**
 * Ruta con sus paradas ordenadas en un sentido (migrada de la tabla legacy `ruta_paradas`).
 * Cada ruta tiene 1-2 filas: una por sentido (nortesur / surnorte).
 */
@Entity(tableName = "ruta_paradas", primaryKeys = ["ruta", "sentido"])
data class RutaParadaEntity(
    val ruta: String,
    val sentido: String,
    val paradas: List<String>,
    val tipo: String,
    val zona: String?,
    val estado: String,
    val trayecto: String?,
    val horario: String?,
)
