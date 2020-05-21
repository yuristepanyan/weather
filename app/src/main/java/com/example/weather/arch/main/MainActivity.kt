package com.example.weather.arch.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.weather.R
import java.lang.Exception

private const val MY_PERMISSIONS_REQUEST_LOCATION = 100

class MainActivity : AppCompatActivity() {
    private var mustCheckGps = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        if(mustCheckGps) checkIsLocationEnabled()
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showPermissionExplanationDialog()
            } else {
                getPermission()
            }
            false
        } else {
            checkIsLocationEnabled()
            true
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.text_location_permission)
                .setPositiveButton(R.string.ok) { _, _ -> getPermission() }
                .show()
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkIsLocationEnabled()
                    }
                } else {
                    showPermissionExplanationDialog()
                }
                return
            }
        }
    }

    private fun checkIsLocationEnabled() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
        }

        if (!gpsEnabled) {
            AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.enable) { _, _ ->
                        run {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            mustCheckGps = true
                        }
                    }
                    .show()
        } else {
            val host = NavHostFragment.create(R.navigation.nav_graph)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.navHost, host)
                    .setPrimaryNavigationFragment(host)
                    .commit()
        }
    }
}
