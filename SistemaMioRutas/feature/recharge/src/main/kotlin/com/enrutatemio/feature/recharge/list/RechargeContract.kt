package com.enrutatemio.feature.recharge.list

import com.enrutatemio.core.common.mvi.UiEffect
import com.enrutatemio.core.common.mvi.UiIntent
import com.enrutatemio.core.common.mvi.UiState
import com.enrutatemio.core.model.RechargePoint

data class RechargeListState(
    val query: String = "",
    val points: List<RechargePoint> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : UiState

sealed interface RechargeListIntent : UiIntent {
    data class Search(val query: String) : RechargeListIntent
    data object Retry : RechargeListIntent
}

sealed interface RechargeListEffect : UiEffect
