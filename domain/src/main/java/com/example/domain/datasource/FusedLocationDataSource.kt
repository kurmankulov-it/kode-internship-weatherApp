package com.example.domain.datasource

import android.location.Location

interface FusedLocationDataSource {
    fun getLocation(): Location?
}