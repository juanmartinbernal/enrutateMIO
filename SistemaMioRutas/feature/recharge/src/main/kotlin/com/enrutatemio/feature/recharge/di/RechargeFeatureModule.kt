package com.enrutatemio.feature.recharge.di

import com.enrutatemio.feature.recharge.list.RechargeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rechargeFeatureModule = module {
    viewModel { RechargeListViewModel(get()) }
}
