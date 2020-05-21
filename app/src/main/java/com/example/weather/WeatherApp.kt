package com.example.weather

import android.app.Application
import com.example.weather.di.dbModule
import com.example.weather.di.networkModule
import com.example.weather.di.weatherFragmentModule
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherApp: Application() {
    private val gson by inject<Gson>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WeatherApp)
            modules(listOf(networkModule, weatherFragmentModule, dbModule))
        }
        Companion.gson = gson
    }

    companion object {
        lateinit var gson: Gson
    }
}