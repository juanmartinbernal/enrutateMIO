package com.enrutatemio.feature.routes.di

import com.enrutatemio.core.model.RouteDirection
import com.enrutatemio.feature.routes.list.RoutesListViewModel
import com.enrutatemio.feature.routes.stops.RouteStopsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val routesFeatureModule = module {
    viewModel { RoutesListViewModel(get()) }
    viewModel { (routeCode: String, direction: RouteDirection) ->
        RouteStopsViewModel(routeCode, direction, get())
    }
}
