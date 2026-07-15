package com.enrutatemio.data.seed

import com.squareup.moshi.JsonClass

/** DTOs para parsear los assets `seed_estaciones.json` / `seed_ruta_paradas.json`. */
@JsonClass(generateAdapter = true)
data class SeedEstacionDto(
    val nombre: String,
    val latitud: Double?,
    val longitud: Double?,
    val listadoRutas: List<String> = emptyList(),
    val tipo: String,
    val direccion: String = "",
)

@JsonClass(generateAdapter = true)
data class SeedRutaParadaDto(
    val ruta: String,
    val sentido: String,
    val paradas: List<String> = emptyList(),
    val tipo: String,
    val zona: String? = null,
    val estado: String = "A",
    val trayecto: String? = null,
    val horario: String? = null,
)
