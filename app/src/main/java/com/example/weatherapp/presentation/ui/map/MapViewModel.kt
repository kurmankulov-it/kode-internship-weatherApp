package com.example.weatherapp.presentation.ui.map

import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.map.GetCityByCoordinates
import com.example.domain.map.GetCityByCoordinatesImpl
import com.example.domain.map.GetUserLocation
import com.example.domain.map.GetUserLocationImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val getUserLocation: GetUserLocation,
    private val getCityByCoordinates: GetCityByCoordinates
) : ViewModel() {

    private val _userLocation: MutableLiveData<Location?> = MutableLiveData()
    val userLocation: LiveData<Location?> get() = _userLocation

    private val _currentCity: MutableLiveData<Address?> = MutableLiveData()
    val currentCity: LiveData<Address?> get() = _currentCity

    fun getUserLocation() {
        viewModelScope.launch {
            _userLocation.value = getUserLocation.execute().single().fold(
                onSuccess = { location ->
                    location
                },
                onFailure = { exception ->
                    throw exception
                }
            )
        }
    }

    fun getCurrentCity(location: LatLng) {
        viewModelScope.launch {
            _currentCity.value = getCityByCoordinates.execute(location).single().fold(
                onSuccess = { address ->
                    address
                },
                onFailure = { exception ->
                    throw exception
                }
            )
        }
    }
}