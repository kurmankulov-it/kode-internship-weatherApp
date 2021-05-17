package com.example.domain.forecast

import com.example.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface GetWeatherByCityName {
    fun execute(cityName: String): Flow<Result<Weather?>>
}