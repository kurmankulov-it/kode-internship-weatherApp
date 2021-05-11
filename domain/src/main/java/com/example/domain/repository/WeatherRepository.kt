package com.example.domain.repository

import android.location.Address
import android.location.Location
import com.example.domain.model.Weather
import com.google.android.gms.maps.model.LatLng

interface WeatherRepository {
    fun getWeatherByCityName(cityName: String): Weather?

    fun getUserLocation(): Location?

    fun getCityByCoordinates(location: LatLng): Address?
}