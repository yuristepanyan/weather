package com.example.weather.arch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weather.model.WeatherResponse

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertData(response: WeatherResponse)

    @Query("SELECT * FROM Response")
    suspend fun getData(): List<WeatherResponse>

    @Query("DELETE FROM Response")
    suspend fun cleanTable()
}