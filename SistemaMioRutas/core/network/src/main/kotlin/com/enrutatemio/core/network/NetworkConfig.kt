package com.enrutatemio.core.network

/**
 * Configuración/credenciales de red inyectada desde el módulo `app` (vía BuildConfig,
 * poblado a partir de `local.properties`/variables de entorno). Ningún secreto se
 * hardcodea en el código fuente, a diferencia de la app legacy (API keys de Google Maps
 * y Foursquare estaban embebidas directamente en el Manifest/código Java).
 */
interface NetworkConfig {
    val googleMapsApiKey: String
    val twitterBearerToken: String
    val enrutateBackendBaseUrl: String
}
