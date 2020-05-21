package com.example.weather.net

import com.example.weather.BuildConfig
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("onecall")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = BuildConfig.APP_ID
    ): Deferred<WeatherResponse>
}