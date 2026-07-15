package com.enrutatemio

import android.app.Application
import com.enrutatemio.core.network.NetworkConfig
import com.enrutatemio.core.network.di.networkModule
import com.enrutatemio.core.database.di.databaseModule
import com.enrutatemio.core.datastore.di.dataStoreModule
import com.enrutatemio.data.di.dataModule
import com.enrutatemio.feature.feeders.di.feedersFeatureModule
import com.enrutatemio.feature.map.di.mapFeatureModule
import com.enrutatemio.feature.news.di.newsFeatureModule
import com.enrutatemio.feature.recharge.di.rechargeFeatureModule
import com.enrutatemio.feature.routes.di.routesFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class EnrutateApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@EnrutateApplication)
            modules(
                networkConfigModule,
                networkModule,
                databaseModule,
                dataStoreModule,
                dataModule,
                routesFeatureModule,
                feedersFeatureModule,
                rechargeFeatureModule,
                mapFeatureModule,
                newsFeatureModule,
            )
        }
    }

    private val networkConfigModule = module {
        single<NetworkConfig> {
            object : NetworkConfig {
                override val googleMapsApiKey: String = BuildConfig.GOOGLE_MAPS_API_KEY
                override val twitterBearerToken: String = BuildConfig.TWITTER_BEARER_TOKEN
                override val enrutateBackendBaseUrl: String = BuildConfig.ENRUTATE_BACKEND_BASE_URL
            }
        }
    }
}
