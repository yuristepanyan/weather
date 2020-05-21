package com.example.weather.arch.dailyWeather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.model.WeatherInfoDaily
import kotlinx.android.synthetic.main.recycler_item_header.*
import kotlinx.android.synthetic.main.recycler_item_header.view.*
import org.koin.android.ext.android.inject

const val CITY_TAG = "cityName"
const val DATA_TAG = "dailyData"

class DailyWeatherFragment : Fragment() {
    private val requestManager by inject<RequestManager>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dailyData = arguments?.getSerializable(DATA_TAG) as WeatherInfoDaily?
        dailyData?.let {
            city.text = arguments?.getString(CITY_TAG)
            temp.text = getString(R.string.celsius_converter, it.temp.getDay())
            pressure.text = getString(R.string.pressure_converter, it.getPressure())
            windSpeed.text = getString(R.string.speed_converter, it.getWindSpeed())
            requestManager.load(it.weather[0].getUrl()).into(view.mainImage)
        }
    }
}