package com.enrutatemio.core.database.di

import androidx.room.Room
import com.enrutatemio.core.database.EnrutateDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            EnrutateDatabase::class.java,
            EnrutateDatabase.DATABASE_NAME,
        ).build()
    }

    single { get<EnrutateDatabase>().estacionDao() }
    single { get<EnrutateDatabase>().rutaParadaDao() }
    single { get<EnrutateDatabase>().noticiaDao() }
}
