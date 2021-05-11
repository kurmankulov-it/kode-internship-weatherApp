package com.example.data.repository

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.example.data.net.OpenWeatherApi
import com.example.data.net.model.asDomainModel
import com.example.domain.model.Weather
import com.example.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks

class WeatherRepositoryImpl(
    private val openWeatherApi: OpenWeatherApi,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder
) : WeatherRepository {

    override fun getWeatherByCityName(cityName: String): Weather? {
        return openWeatherApi.getWeatherByCityName(cityName).execute().body()?.asDomainModel()
    }

    @SuppressLint("MissingPermission")
    override fun getUserLocation(): Location? {
        return Tasks.await(fusedLocationProviderClient.lastLocation)
    }

    override fun getCityByCoordinates(location: LatLng): Address? {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses.isNotEmpty()) {
            addresses.first()
        } else {
            null
        }
    }
}