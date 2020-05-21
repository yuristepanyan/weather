package com.example.weather.di

import android.content.Context
import androidx.room.Room
import com.example.weather.BuildConfig
import com.example.weather.arch.db.WeatherDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    single { createDb(androidApplication()) }
    single { createWeatherDao(get()) }
}

private fun createDb(context: Context) = Room.databaseBuilder(
        context,
        WeatherDb::class.java,
        BuildConfig.DATABASE_NAME
    )
    .build()


private fun createWeatherDao(db: WeatherDb) = db.weatherDao()