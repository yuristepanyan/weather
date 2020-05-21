package com.example.weather.model

import com.example.weather.extensions.getStringValue
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherInfoDaily(
    @SerializedName("dt")
    val date: Long,
    val weather: ArrayList<Weather>,
    val temp: Temperature,
    private val pressure: Float,
    private val windSpeed: Float
) : Serializable {

    fun getPressure() = pressure.getStringValue()

    fun getWindSpeed() = windSpeed.getStringValue()
}