package com.example.data.repository

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.example.data.net.OpenWeatherApi
import com.example.data.net.model.asDomainModel
import com.example.domain.datasource.FusedLocationDataSource
import com.example.domain.datasource.GeocoderDataSource
import com.example.domain.model.Weather
import com.example.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks

class WeatherRepositoryImpl(
    private val openWeatherApi: OpenWeatherApi,
    private val fusedLocationDataSource: FusedLocationDataSource,
    private val geocoderDataSource: GeocoderDataSource
) : WeatherRepository {

    override fun getWeatherByCityName(cityName: String): Weather? {
        return openWeatherApi.getWeatherByCityName(cityName).execute().body()?.asDomainModel()
    }

    override fun getUserLocation(): Location? {
        return fusedLocationDataSource.getLocation()
    }

    override fun getCityByCoordinates(location: LatLng): Address? {
        return geocoderDataSource.getAddress(location)
    }
}