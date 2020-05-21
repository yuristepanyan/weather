package com.example.weather.di

import android.content.Context
import com.example.weather.arch.weather.WeatherViewModel
import com.example.weather.arch.weather.adapter.WeatherAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherFragmentModule = module {

    viewModel { WeatherViewModel(get(), get()) }

    factory { (context: Context) -> WeatherAdapter(context, get()) }
}