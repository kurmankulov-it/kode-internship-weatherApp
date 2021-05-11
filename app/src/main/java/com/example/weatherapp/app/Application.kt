package com.example.weatherapp.app

import android.app.Application
import com.example.weatherapp.app.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    dataModule,
                    useCaseModule,
                    networkModule,
                    viewModelModule,
                    locationModule
                )
            )
        }
    }
}