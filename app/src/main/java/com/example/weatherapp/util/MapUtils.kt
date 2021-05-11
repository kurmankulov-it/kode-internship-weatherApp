package com.example.weatherapp.util

import android.content.Context
import com.example.weatherapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object MapUtils {
    fun GoogleMap.addPin(location: LatLng) {
        addMarker(
            MarkerOptions()
                .position(location)
                .title(location.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                .anchor(0.5f, 0.5f)
        )
    }
}

val Context.isConnected: Boolean
    get() {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else -> {
                // Use depreciated methods only on older devices
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                nwInfo.isConnected
            }
        }
    }