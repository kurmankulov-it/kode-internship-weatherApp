package com.example.domain.forecast

import com.example.domain.model.Weather
import com.example.domain.repository.WeatherRepository
import com.example.domain.util.WeatherByCityNameNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetWeatherByCityNameImpl(private val weatherRepository: WeatherRepository) :
    GetWeatherByCityName {
    override fun execute(cityName: String): Flow<Result<Weather?>> = flow {
        val result = try {
            Result.success(weatherRepository.getWeatherByCityName(cityName))
        } catch (e: WeatherByCityNameNotFound) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}