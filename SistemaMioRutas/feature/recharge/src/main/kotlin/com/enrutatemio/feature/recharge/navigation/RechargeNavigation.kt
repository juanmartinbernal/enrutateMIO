package com.enrutatemio.feature.recharge.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.feature.recharge.list.RechargeListRoute

const val RECHARGE_ROUTE = "recharge"

fun NavGraphBuilder.rechargeScreen() {
    composable(RECHARGE_ROUTE) {
        RechargeListRoute()
    }
}
