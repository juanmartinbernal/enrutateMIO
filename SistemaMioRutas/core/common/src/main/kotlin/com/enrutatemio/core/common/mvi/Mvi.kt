package com.enrutatemio.core.common.mvi

/** Marca un estado inmutable de UI (el "Model" en MVI). */
interface UiState

/** Marca una intención/acción disparada desde la UI (el "Intent" en MVI). */
interface UiIntent

/** Marca un efecto de un solo disparo (navegación, snackbar, etc.), no forma parte del estado. */
interface UiEffect
