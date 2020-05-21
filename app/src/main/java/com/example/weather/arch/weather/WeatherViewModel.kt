package com.example.weather.arch.weather

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.arch.db.WeatherDao
import com.example.weather.model.WeatherResponse
import com.example.weather.net.WeatherApi
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val api: WeatherApi,
    private val weatherDao: WeatherDao
) : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val weather = MutableLiveData<WeatherResponse>()
    val error = MutableLiveData<String>()
    val showNoInternet = MutableLiveData<Unit>()

    private var location: Location? = null

    var loadingIsShown = false
    var mustShowLoading = true
    var networkAvailable = false

    fun getWeather(location: Location? = null) {
        location?.let { this.location = it }
        if (this.location == null) return
        if(networkAvailable) {
            viewModelScope.launch {
                runCatching {
                    if(mustShowLoading) loading.postValue(true)
                    mustShowLoading = true
                    api.getWeather(
                        this@WeatherViewModel.location!!.latitude,
                        this@WeatherViewModel.location!!.longitude
                    )
                }.onSuccess {
                    try {
                        val data = it.await()
                        insert(data)
                        weather.postValue(data)
                        loading.postValue(false)
                    } catch (e: Throwable) {
                        handleError(e)
                    }
                }.onFailure {
                    handleError(it)
                }
            }
        } else {
            getWeatherFromDb()
        }
    }

    fun getWeatherFromDb() {
        viewModelScope.launch {
            val data = weatherDao.getData()
            if(data.isEmpty()) {
                showNoInternet.postValue(Unit)
                loading.postValue(false)
                return@launch
            }
            weather.postValue(data[0])
            loading.postValue(false)
        }
    }

    private suspend fun insert(response: WeatherResponse) {
        weatherDao.cleanTable()
        weatherDao.insertData(response)
    }

    private fun handleError(exception: Throwable) {
        error.postValue(exception.message)
        loading.postValue(false)
    }
}