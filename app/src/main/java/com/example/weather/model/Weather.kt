package com.example.weather.model

import com.example.weather.BuildConfig
import java.io.Serializable

data class Weather(
    val main: String,
    val icon: String
) : Serializable {
    fun getUrl() = BuildConfig.IMAGE_BASE_URL + icon + "@2x.png"
}