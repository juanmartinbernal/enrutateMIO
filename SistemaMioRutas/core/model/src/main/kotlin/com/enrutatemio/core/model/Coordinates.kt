package com.enrutatemio.core.model

/**
 * Coordenada geográfica agnóstica de plataforma (no depende de Google Play Services)
 * para poder usarse desde `domain`/`data` sin acoplarse a Android.
 */
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
)
