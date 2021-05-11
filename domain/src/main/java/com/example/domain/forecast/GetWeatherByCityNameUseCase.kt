package com.example.domain.forecast

import com.example.domain.model.Weather
import com.example.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetWeatherByCityNameUseCase(private val weatherRepository: WeatherRepository) {
    fun execute(cityName: String): Flow<Result<Weather?>> = flow {
        val result = try {
            Result.success(weatherRepository.getWeatherByCityName(cityName))
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}