package com.example.domain.map

import android.location.Location
import com.example.domain.repository.WeatherRepository
import com.example.domain.util.UserLocationNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class GetUserLocationImpl(private val weatherRepository: WeatherRepository) : GetUserLocation {
    override fun execute(): Flow<Result<Location?>> = flow {
        val result = try {
            Result.success(weatherRepository.getUserLocation())
        } catch (e: UserLocationNotFound) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}