package com.example.weather.model

import com.example.weather.extensions.getStringValue
import com.google.gson.annotations.SerializedName

data class WeatherInfoCurrent(
    @SerializedName("dt")
    val date: Long,
    val weather: ArrayList<Weather>,
    private val temp: Float,
    private val pressure: Float,
    private val windSpeed: Float
) {

    fun getTemp() = temp.getStringValue()

    fun getPressure() = pressure.getStringValue()

    fun getWindSpeed() = windSpeed.getStringValue()

}