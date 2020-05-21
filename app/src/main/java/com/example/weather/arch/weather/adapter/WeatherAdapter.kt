package com.example.weather.arch.weather.adapter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.extensions.inflate
import com.example.weather.model.WeatherInfoCurrent
import com.example.weather.model.WeatherInfoDaily
import com.example.weather.model.WeatherResponse
import kotlinx.android.synthetic.main.recycler_item_day.view.*
import kotlinx.android.synthetic.main.recycler_item_header.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(val context: Context, val requestManager: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: WeatherAdapterListener? = null

    private lateinit var city: String
    private val headerType = 0
    private val itemType = 1

    var data: WeatherResponse? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == headerType) {
            return HeaderViewHolder(parent.inflate(R.layout.recycler_item_header))
        }
        return DailyViewHolder(parent.inflate(R.layout.recycler_item_day))
    }

    override fun getItemCount() = if (data != null) 8 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == headerType) {
            (holder as HeaderViewHolder).bind(data!!.current)
        } else {
            (holder as DailyViewHolder).bind(data!!.daily[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) headerType else itemType
    }

    interface WeatherAdapterListener {
        fun dayClicked(city: String, dailyWeather: WeatherInfoDaily)
    }

    inner class HeaderViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(current: WeatherInfoCurrent) {
            city = getCity()
            view.city.text = city
            view.temp.text = context.getString(R.string.celsius_converter, current.getTemp())
            view.pressure.text =
                context.getString(R.string.pressure_converter, current.getPressure())
            view.windSpeed.text =
                context.getString(R.string.speed_converter, current.getWindSpeed())
            requestManager.load(current.weather[0].getUrl()).into(view.mainImage)
        }

        private fun getCity(): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(data!!.lat, data!!.lon, 1)
            return addresses[0].locality
        }
    }

    inner class DailyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                if(adapterPosition > 1) listener?.dayClicked(city, data!!.daily[adapterPosition - 1])
            }
        }

        fun bind(daily: WeatherInfoDaily) {
            val weather = daily.weather[0]
            view.dailyTemp.text = context.getString(
                R.string.temp_converter,
                daily.temp.getDay(),
                daily.temp.getNight()
            )
            view.dailyDesc.text =
                context.getString(R.string.daily_desc_converter, getDay(daily.date), weather.main)
            requestManager.load(weather.getUrl()).into(view.imageDaily)
        }

        private fun getDay(seconds: Long) = when (adapterPosition) {
            1 -> context.getString(R.string.today)
            2 -> context.getString(R.string.tomorrow)
            else -> {
                val formatter = SimpleDateFormat("EEE", Locale.getDefault())
                val date = Date(seconds * 1000)
                formatter.format(date)
            }
        }
    }
}