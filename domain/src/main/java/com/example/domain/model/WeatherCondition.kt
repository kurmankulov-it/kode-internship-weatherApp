package com.example.domain.model

enum class WeatherCondition(val value: Int) {
    CLEAR_SKY(1),
    FEW_CLOUDS(2),
    SCATTERED_CLOUDS(3),
    BROKEN_CLOUDS(4),
    SHOWER_RAIN(9),
    RAIN(10),
    THUNDERSTORM(11),
    SNOW(13),
    MIST(50),
    DEFAULT(0);

    companion object {
        fun fromInt(value: Int) = WeatherCondition.values().first { it.value == value }
    }
}