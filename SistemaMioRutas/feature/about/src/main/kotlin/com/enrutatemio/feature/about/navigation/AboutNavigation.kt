package com.enrutatemio.feature.about.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.feature.about.AboutRoute

const val ABOUT_ROUTE = "about"

fun NavGraphBuilder.aboutScreen() {
    composable(ABOUT_ROUTE) {
        AboutRoute()
    }
}
