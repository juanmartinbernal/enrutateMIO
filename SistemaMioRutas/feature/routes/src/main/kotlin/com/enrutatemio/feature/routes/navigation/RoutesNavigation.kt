package com.enrutatemio.feature.routes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.compose.composable
import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.feature.routes.list.RoutesListRoute
import com.enrutatemio.feature.routes.stops.RouteStopsRoute

const val ROUTES_GRAPH_ROUTE = "routes_graph"
private const val ROUTES_LIST_ROUTE = "routes_list"
private const val ROUTE_STOPS_ROUTE = "route_stops"
private const val ARG_ROUTE_CODE = "routeCode"
private const val ARG_DIRECTION = "direction"

/** Ruta de navegación pública hacia el detalle de paradas, reutilizable desde otras features (ej. alimentadores). */
fun routeStopsDestination(routeCode: String, direction: RouteDirection): String =
    "$ROUTE_STOPS_ROUTE/$routeCode/${direction.name}"

fun NavGraphBuilder.routesGraph(navController: NavController) {
    navigation(startDestination = ROUTES_LIST_ROUTE, route = ROUTES_GRAPH_ROUTE) {
        composable(ROUTES_LIST_ROUTE) {
            RoutesListRoute(
                onNavigateToStops = { code, direction ->
                    navController.navigate(routeStopsDestination(code, direction))
                },
            )
        }
        composable(
            route = "$ROUTE_STOPS_ROUTE/{$ARG_ROUTE_CODE}/{$ARG_DIRECTION}",
            arguments = listOf(
                navArgument(ARG_ROUTE_CODE) { type = NavType.StringType },
                navArgument(ARG_DIRECTION) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString(ARG_ROUTE_CODE).orEmpty()
            val direction = RouteDirection.valueOf(
                backStackEntry.arguments?.getString(ARG_DIRECTION) ?: RouteDirection.NORTE_SUR.name,
            )
            RouteStopsRoute(routeCode = code, direction = direction, onBack = navController::popBackStack)
        }
    }
}
