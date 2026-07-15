package com.enrutatemio.core.model

/**
 * Tipo de ruta del sistema MIO, inferido del prefijo del código de ruta legacy
 * (E=Expreso, P=Pretroncal, T=Troncal, A=Alimentador).
 */
enum class RouteType(val dbValue: String, val prefix: Char) {
    EXPRESO(dbValue = "expreso", prefix = 'E'),
    PRETRONCAL(dbValue = "pretroncal", prefix = 'P'),
    TRONCAL(dbValue = "troncal", prefix = 'T'),
    ALIMENTADOR(dbValue = "alimentador", prefix = 'A'),
    ;

    companion object {
        fun fromRouteCode(code: String): RouteType? =
            entries.firstOrNull { it.prefix == code.trim().uppercase().firstOrNull() }
    }
}

/** Sentido de recorrido de una ruta. */
enum class RouteDirection(val dbValue: String) {
    SUR_NORTE(dbValue = "surnorte"),
    NORTE_SUR(dbValue = "nortesur"),
}

/** Zona geográfica de la ciudad, usada para filtrar rutas alimentadoras. */
enum class Zone(val dbValue: String, val displayName: String) {
    TODAS(dbValue = "", displayName = "Todas"),
    CENTRO(dbValue = "centro", displayName = "Centro"),
    NORTE(dbValue = "norte", displayName = "Norte"),
    SUR(dbValue = "sur", displayName = "Sur"),
    ORIENTE(dbValue = "oriente", displayName = "Oriente"),
    OCCIDENTE(dbValue = "occidente", displayName = "Occidente"),
}
