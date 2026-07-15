package com.enrutatemio.feature.map.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.feature.map.trip.MapTripRoute

const val MAP_ROUTE = "map"

fun NavGraphBuilder.mapScreen() {
    composable(MAP_ROUTE) {
        MapTripRoute()
    }
}
