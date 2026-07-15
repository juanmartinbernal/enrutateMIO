package com.enrutatemio.feature.news.di

import com.enrutatemio.feature.news.list.NewsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsFeatureModule = module {
    viewModel { NewsListViewModel(get(), get(), get()) }
}
