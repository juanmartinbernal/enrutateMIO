package com.enrutatemio.feature.feeders.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.feature.feeders.list.FeedersRoute
import com.enrutatemio.feature.routes.navigation.routeStopsDestination

const val FEEDERS_ROUTE = "feeders"

fun NavGraphBuilder.feedersScreen(navController: NavController) {
    composable(FEEDERS_ROUTE) {
        FeedersRoute(
            onNavigateToStops = { code, direction ->
                navController.navigate(routeStopsDestination(code, direction))
            },
        )
    }
}
