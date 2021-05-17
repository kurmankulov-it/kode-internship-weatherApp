package com.example.domain.map

import android.location.Address
import com.example.domain.repository.WeatherRepository
import com.example.domain.util.CityByCoordinatesNotFound
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCityByCoordinatesImpl(private val weatherRepository: WeatherRepository) :
    GetCityByCoordinates {
    override fun execute(location: LatLng): Flow<Result<Address?>> = flow {
        val result = try {
            Result.success(weatherRepository.getCityByCoordinates(location))
        } catch (e: CityByCoordinatesNotFound) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}