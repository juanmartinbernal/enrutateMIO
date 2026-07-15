package com.enrutatemio.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores migrada 1:1 desde la app legacy, donde cada tipo de ruta del
 * sistema MIO tenía un color fijo hardcodeado en los adapters (`ListaRutas`, `RutasAdapter`).
 */
object EnrutateColors {
    val RouteExpreso = Color(0xFFECDF3E) // amarillo - E
    val RoutePretroncal = Color(0xFF2E79B9) // azul - P
    val RouteTroncal = Color(0xFFE30613) // rojo - T
    val RouteAlimentador = Color(0xFF5FB985) // verde - A

    val WalkPolyline = Color(0xFF78AB59) // verde caminar (Section GIS_ROUTE/WALK)
    val JourneyPolyline = Color(0xFF6F8EBF) // azul viaje en bus (Section JOURNEY)

    val Primary = Color(0xFF214868) // azul oscuro usado en textos de estaciones/paradas
    val PrimaryVariant = Color(0xFF1A3A54)
    val Secondary = RouteTroncal

    val Background = Color(0xFFF5F6FA)
    val Surface = Color.White
    val OnSurfaceMuted = Color(0xFF88999E)
}
