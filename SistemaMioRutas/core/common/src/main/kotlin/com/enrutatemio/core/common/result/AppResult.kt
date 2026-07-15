package com.enrutatemio.core.common.result

/**
 * Resultado de una operación asíncrona (repositorio/use case), evita lanzar excepciones
 * a través de las capas y fuerza el manejo explícito de errores en la UI.
 */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val throwable: Throwable, val message: String? = null) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (Throwable, String?) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(throwable, message)
    return this
}

fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.data

suspend fun <T> resultOf(block: suspend () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (t: Throwable) {
    AppResult.Error(t, t.message)
}
