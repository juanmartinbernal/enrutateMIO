package com.enrutatemio.feature.integration.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.feature.integration.IntegrationRoute

const val INTEGRATION_ROUTE = "integration"

fun NavGraphBuilder.integrationScreen() {
    composable(INTEGRATION_ROUTE) {
        IntegrationRoute()
    }
}
