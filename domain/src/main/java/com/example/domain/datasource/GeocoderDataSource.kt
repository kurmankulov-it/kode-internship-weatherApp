package com.example.domain.datasource

import android.location.Address
import com.google.android.gms.maps.model.LatLng

interface GeocoderDataSource {
    fun getAddress(location: LatLng): Address?
}