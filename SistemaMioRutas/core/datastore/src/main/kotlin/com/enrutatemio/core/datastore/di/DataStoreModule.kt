package com.enrutatemio.core.datastore.di

import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.enrutatemio.core.datastore.UserPreferencesDataStore

private const val PREFERENCES_NAME = "enrutatemio_preferences"

private val android.content.Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

val dataStoreModule = module {
    single { androidContext().dataStore }
    single { UserPreferencesDataStore(get()) }
}
