package com.example.weather.arch.db

import androidx.room.TypeConverter
import com.example.weather.WeatherApp
import com.example.weather.model.Temperature
import com.example.weather.model.Weather
import com.example.weather.model.WeatherInfoCurrent
import com.example.weather.model.WeatherInfoDaily
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters() {
    private val gson = WeatherApp.gson

    @TypeConverter
    fun stringToTemp(value: String): Temperature {
        return gson.fromJson(value, Temperature::class.java)
    }

    @TypeConverter
    fun tempToString(value: Temperature): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToDaily(value: String): WeatherInfoDaily {
        return gson.fromJson(value, WeatherInfoDaily::class.java)
    }

    @TypeConverter
    fun dailyToString(value: WeatherInfoDaily): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToCurrent(value: String): WeatherInfoCurrent {
        return gson.fromJson(value, WeatherInfoCurrent::class.java)
    }

    @TypeConverter
    fun currentToString(value: WeatherInfoCurrent): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToWeather(value: String): Weather {
        return gson.fromJson(value, Weather::class.java)
    }

    @TypeConverter
    fun weatherToString(value: Weather): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToArrayListDaily(value: String): ArrayList<WeatherInfoDaily> {
        val listType: Type = object : TypeToken<ArrayList<WeatherInfoDaily>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun arrayListDailyToString(value: ArrayList<WeatherInfoDaily>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToArrayListWeather(value: String): ArrayList<Weather> {
        val listType: Type = object : TypeToken<ArrayList<Weather>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun arrayListWeatherToString(value: ArrayList<Weather>): String {
        return gson.toJson(value)
    }
}