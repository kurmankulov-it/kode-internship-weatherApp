package com.example.weatherapp.presentation.ui.map

import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.map.GetCityByCoordinatesUseCase
import com.example.domain.map.GetUserLocationUseCase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val getCityByCoordinatesUseCase: GetCityByCoordinatesUseCase
) : ViewModel() {

    private val _userLocation: MutableLiveData<Result<Location?>> = MutableLiveData()
    val userLocation: LiveData<Result<Location?>> get() = _userLocation

    private val _currentCity: MutableLiveData<Result<Address?>> = MutableLiveData()
    val currentCity: LiveData<Result<Address?>> get() = _currentCity

    fun getUserLocation() {
        viewModelScope.launch {
            _userLocation.value = getUserLocationUseCase.execute().single()
        }
    }

    fun getCurrentCity(location: LatLng) {
        viewModelScope.launch {
            _currentCity.value = getCityByCoordinatesUseCase.execute(location).single()
        }
    }
}