package com.enrutatemio.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/** Calificadores para inyectar dispatchers de coroutines vía Koin, facilitando tests. */
enum class EnrutateDispatcher {
    Default,
    IO,
    Main,
}

/** Envoltorio inyectable para no depender directamente de `Dispatchers.X` en el código de negocio. */
data class DispatcherProvider(
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
