package com.example.data.datasource

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.domain.datasource.GeocoderDataSource
import com.google.android.gms.maps.model.LatLng

class GeocoderDataSourceImpl(context: Context) : GeocoderDataSource {
    private val geocoder = Geocoder(context)

    override fun getAddress(location: LatLng): Address? {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses.isNotEmpty()) {
            addresses.first()
        } else {
            null
        }
    }
}