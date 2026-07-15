package com.enrutatemio.feature.map.di

import com.enrutatemio.feature.map.trip.MapTripViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapFeatureModule = module {
    viewModel { MapTripViewModel(get(), get(), get()) }
}
