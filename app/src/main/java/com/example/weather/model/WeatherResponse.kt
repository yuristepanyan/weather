package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Response")
data class WeatherResponse(
    @PrimaryKey
    val lat: Double,
    val lon: Double,
    val current: WeatherInfoCurrent,
    val daily: ArrayList<WeatherInfoDaily>
)