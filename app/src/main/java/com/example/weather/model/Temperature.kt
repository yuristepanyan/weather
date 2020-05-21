package com.example.weather.model

import com.example.weather.extensions.getStringValue
import java.io.Serializable

data class Temperature(
    val day: Float,
    val night: Float
) : Serializable {

    fun getDay() = day.getStringValue()
    fun getNight() = night.getStringValue()
}