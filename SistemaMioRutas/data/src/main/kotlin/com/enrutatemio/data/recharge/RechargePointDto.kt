package com.enrutatemio.data.recharge

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/** DTO fiel al JSON legacy `puntos_recarga.json` (claves en MAYÚSCULAS). */
@JsonClass(generateAdapter = true)
data class RechargePointDto(
    @Json(name = "NUMERO") val numero: Int = 0,
    @Json(name = "NOMBRE") val nombre: String = "",
    @Json(name = "DIRECCION") val direccion: String = "",
    @Json(name = "LATITUD") val latitud: Double = 0.0,
    @Json(name = "LONGITUD") val longitud: Double = 0.0,
    @Json(name = "TIPO") val tipo: String = "",
)
