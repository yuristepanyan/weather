package com.example.weather.arch.weather

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.weather.R
import com.example.weather.arch.dailyWeather.CITY_TAG
import com.example.weather.arch.dailyWeather.DATA_TAG
import com.example.weather.arch.weather.adapter.WeatherAdapter
import com.example.weather.model.WeatherInfoDaily
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.novoda.merlin.Merlin
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class WeatherFragment : Fragment(), WeatherAdapter.WeatherAdapterListener {
    private val viewModel by inject<WeatherViewModel>()
    private val adapter: WeatherAdapter by inject { parametersOf(context) }

    private var merlin: Merlin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        merlin = Merlin.Builder().withConnectableCallbacks().build(context)

        merlin?.registerConnectable {
            viewModel.networkAvailable = true
            checkLocation()
            viewModel.mustShowLoading = adapter.data == null
        }
    }

    override fun onResume() {
        super.onResume()
        merlin?.bind()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initView()
        viewModel.getWeatherFromDb()
    }

    override fun onPause() {
        super.onPause()
        merlin?.unbind()
    }

    private fun initView() {
        adapter.listener = this
        recycler.adapter = adapter
        swipeToRefresh.setOnRefreshListener {
            if (viewModel.networkAvailable) {
                viewModel.getWeather()
            } else {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    private fun bind() {
        viewModel.showNoInternet.observe(viewLifecycleOwner, Observer {
            Handler().postDelayed({
                if (viewModel.networkAvailable) return@postDelayed
                AlertDialog.Builder(context)
                    .setMessage(R.string.no_internet_message)
                    .setPositiveButton(R.string.ok, null)
                    .show()
            }, 1500)
        })

        viewModel.weather.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (!viewModel.loadingIsShown) {
                if (it == true) {
                    loading.visibility = View.VISIBLE
                } else {
                    loading.visibility = View.GONE
                    viewModel.loadingIsShown = true
                }
            } else {
                swipeToRefresh.isRefreshing = it == true
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Snackbar.make(recycler, it ?: "", Snackbar.LENGTH_LONG).show()
        })
    }

    private fun checkLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        LocationServices.getFusedLocationProviderClient(requireContext())
                            .removeLocationUpdates(this)
                        viewModel.getWeather(location)
                    }
                    break
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun dayClicked(city: String, dailyWeather: WeatherInfoDaily) {
        view?.findNavController()
            ?.navigate(R.id.action_weatherFragment_to_dailyWeatherFragment,
                bundleOf(DATA_TAG to dailyWeather, CITY_TAG to city))
    }
}
