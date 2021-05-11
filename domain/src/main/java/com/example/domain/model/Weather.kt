package com.example.domain.model

data class Weather(
    val temperature: Double?,
    val icon: String?,
    val type: String?,
    val humidity: Double?,
    val windSpeed: Double?,
    val windDeg: Int?,
    val pressure: Double?,
    val description: String?,
    val condition: WeatherCondition?
)