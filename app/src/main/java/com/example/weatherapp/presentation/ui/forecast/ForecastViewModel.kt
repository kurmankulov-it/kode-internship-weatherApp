package com.example.weatherapp.presentation.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.forecast.GetWeatherByCityNameUseCase
import com.example.domain.model.Weather
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getWeatherByCityNameUseCase: GetWeatherByCityNameUseCase
) : ViewModel() {

    private val _weather: MutableLiveData<Weather?> = MutableLiveData()
    val weather: LiveData<Weather?> get() = _weather

    fun getWeather(cityName: String) {
        viewModelScope.launch {
            _weather.value = getWeatherByCityNameUseCase.execute(cityName).single().getOrNull()
        }
    }
}