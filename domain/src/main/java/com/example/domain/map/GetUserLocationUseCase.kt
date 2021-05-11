package com.example.domain.map

import android.location.Location
import com.example.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class GetUserLocationUseCase(private val weatherRepository: WeatherRepository) {
    fun execute(): Flow<Result<Location?>> = flow {
        val result = try {
            Result.success(weatherRepository.getUserLocation())
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}