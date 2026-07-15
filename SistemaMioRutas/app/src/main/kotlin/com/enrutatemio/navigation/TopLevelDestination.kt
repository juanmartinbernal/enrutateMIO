package com.enrutatemio.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector
import com.enrutatemio.feature.feeders.navigation.FEEDERS_ROUTE
import com.enrutatemio.feature.map.navigation.MAP_ROUTE
import com.enrutatemio.feature.news.navigation.NEWS_ROUTE
import com.enrutatemio.feature.recharge.navigation.RECHARGE_ROUTE
import com.enrutatemio.feature.routes.navigation.ROUTES_GRAPH_ROUTE

/** Destinos principales de la barra de navegación inferior, reemplazando el SlidingMenu legacy. */
enum class TopLevelDestination(val route: String, val label: String, val icon: ImageVector) {
    ROUTES(ROUTES_GRAPH_ROUTE, "Rutas", Icons.Default.DirectionsBus),
    FEEDERS(FEEDERS_ROUTE, "Alimentadores", Icons.Default.AltRoute),
    MAP(MAP_ROUTE, "Mapa", Icons.Default.Map),
    NEWS(NEWS_ROUTE, "Noticias", Icons.Default.Newspaper),
    RECHARGE(RECHARGE_ROUTE, "Recarga", Icons.Default.CreditCard),
}
