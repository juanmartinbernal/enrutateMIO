package com.enrutatemio.feature.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enrutatemio.feature.news.list.NewsListRoute

const val NEWS_ROUTE = "news"

fun NavGraphBuilder.newsScreen() {
    composable(NEWS_ROUTE) {
        NewsListRoute()
    }
}
