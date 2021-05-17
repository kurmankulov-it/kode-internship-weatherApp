package com.example.data.net

import com.example.data.BuildConfig
import com.example.data.net.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val OPEN_WEATHER_UNITS = "metric"

interface OpenWeatherApi {
    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("units") units: String? = OPEN_WEATHER_UNITS,
        @Query("appid") appid: String? = BuildConfig.OW_KEY,
    ): Call<WeatherResponse>
}