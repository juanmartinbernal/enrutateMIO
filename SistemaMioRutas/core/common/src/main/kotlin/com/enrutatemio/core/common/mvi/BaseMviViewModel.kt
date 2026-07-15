package com.enrutatemio.core.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel base siguiendo el patrón MVI unidireccional:
 * - [state] expone el estado inmutable actual (StateFlow, sobrevive rotación/recomposición).
 * - [effect] expone eventos de un solo disparo (SharedFlow respaldado por Channel).
 * - [onIntent] es el único punto de entrada para que la UI dispare acciones.
 *
 * Toda la lógica de features de la app (rutas, mapa, noticias, recarga, alimentadores)
 * extiende esta clase para mantener un flujo de datos unidireccional y testeable.
 */
abstract class BaseMviViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    val currentState: S get() = _state.value

    private val effectChannel = Channel<E>(Channel.BUFFERED)
    val effect = effectChannel.receiveAsFlow()

    /** Punto de entrada único desde la UI (Composable) para disparar una intención. */
    abstract fun onIntent(intent: I)

    protected fun setState(reducer: S.() -> S) {
        _state.value = currentState.reducer()
    }

    protected fun sendEffect(builder: () -> E) {
        viewModelScope.launch {
            effectChannel.send(builder())
        }
    }
}
