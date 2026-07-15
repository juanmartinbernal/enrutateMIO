package com.enrutatemio.data.di

import com.enrutatemio.data.feeders.FeedersRepositoryImpl
import com.enrutatemio.data.map.DirectionsRepositoryImpl
import com.enrutatemio.data.news.NewsRepositoryImpl
import com.enrutatemio.data.recharge.RechargeRepositoryImpl
import com.enrutatemio.data.routes.RoutesRepositoryImpl
import com.enrutatemio.data.seed.DatabaseSeeder
import com.enrutatemio.domain.feeders.FeedersRepository
import com.enrutatemio.domain.feeders.GetFeederRoutesUseCase
import com.enrutatemio.domain.map.DirectionsRepository
import com.enrutatemio.domain.map.GetTransitDirectionsUseCase
import com.enrutatemio.domain.map.GetWalkingDirectionsUseCase
import com.enrutatemio.domain.news.MarkNewsAsReadUseCase
import com.enrutatemio.domain.news.NewsRepository
import com.enrutatemio.domain.news.ObserveNewsUseCase
import com.enrutatemio.domain.news.RefreshNewsUseCase
import com.enrutatemio.domain.recharge.GetNearbyRechargePointsUseCase
import com.enrutatemio.domain.recharge.GetRechargePointsUseCase
import com.enrutatemio.domain.recharge.RechargeRepository
import com.enrutatemio.domain.routes.GetAllStationsUseCase
import com.enrutatemio.domain.routes.GetRouteStopsUseCase
import com.enrutatemio.domain.routes.GetRoutesByTypeUseCase
import com.enrutatemio.domain.routes.GetStationDetailsUseCase
import com.enrutatemio.domain.routes.RoutesRepository
import com.enrutatemio.domain.routes.SearchStationsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single { DatabaseSeeder(androidContext(), get(), get(), get()) }

    // Repositorios
    single<RoutesRepository> { RoutesRepositoryImpl(get(), get(), get()) }
    single<FeedersRepository> { FeedersRepositoryImpl(get(), get()) }
    single<RechargeRepository> { RechargeRepositoryImpl(androidContext(), get()) }
    single<DirectionsRepository> { DirectionsRepositoryImpl(get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get()) }

    // Use cases - rutas
    factory { GetRoutesByTypeUseCase(get()) }
    factory { GetRouteStopsUseCase(get()) }
    factory { SearchStationsUseCase(get()) }
    factory { GetAllStationsUseCase(get()) }
    factory { GetStationDetailsUseCase(get()) }

    // Use cases - alimentadores
    factory { GetFeederRoutesUseCase(get()) }

    // Use cases - recarga
    factory { GetRechargePointsUseCase(get()) }
    factory { GetNearbyRechargePointsUseCase(get()) }

    // Use cases - mapa/direcciones
    factory { GetWalkingDirectionsUseCase(get()) }
    factory { GetTransitDirectionsUseCase(get()) }

    // Use cases - noticias
    factory { ObserveNewsUseCase(get()) }
    factory { RefreshNewsUseCase(get()) }
    factory { MarkNewsAsReadUseCase(get()) }
}
