package com.example.domain.map

import android.location.Address
import com.example.domain.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class GetCityByCoordinatesUseCase(private val weatherRepository: WeatherRepository) {
    fun execute(location: LatLng): Flow<Result<Address?>> = flow {
        val result = try {
            Result.success(weatherRepository.getCityByCoordinates(location))
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}