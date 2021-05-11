package com.example.data.net.model

import com.example.data.util.Constants.ICON_URL
import com.example.data.util.Constants.ICON_URL_EXT
import com.example.domain.model.WeatherCondition
import com.example.domain.model.Weather
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    val coord: Coordinates,

    @SerializedName("weather")
    val weather: List<WeatherInfo>,

    @SerializedName("base")
    val base: String,

    @SerializedName("main")
    val main: MainInfo,

    @SerializedName("visibility")
    val visibility: Long,

    @SerializedName("wind")
    val wind: WindInfo,

    @SerializedName("clouds")
    val clouds: CloudInfo,

    @SerializedName("dt")
    val dt: Long,

    @SerializedName("sys")
    val sys: SysInfo,

    @SerializedName("timezone")
    val timeZone: Int,

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("cod")
    val cod: Int
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainInfo(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double
)

data class WindInfo(
    val speed: Double,
    val deg: Int
)

data class CloudInfo(
    val all: Int
)

data class SysInfo(
    val type: Int,
    val id: Int,
    val message: Double,
    val country: String,
    val sunrise: Double,
    val sunset: Double
)

fun WeatherResponse.asDomainModel(): Weather {
    return Weather(
        temperature = main.temp,
        type = weather.first().main,
        description = weather.first().description,
        icon = ICON_URL + weather.first().icon + ICON_URL_EXT,
        humidity = main.humidity,
        windSpeed = wind.speed,
        windDeg = wind.deg,
        pressure = main.pressure,
        condition = weatherCondition(weather.first().icon.substring(0, 2).toInt())
    )
}

private fun weatherCondition(value: Int): WeatherCondition {
    return WeatherCondition.fromInt(value)
}