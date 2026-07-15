package com.enrutatemio.feature.feeders.di

import com.enrutatemio.feature.feeders.list.FeedersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedersFeatureModule = module {
    viewModel { FeedersViewModel(get(), get()) }
}
