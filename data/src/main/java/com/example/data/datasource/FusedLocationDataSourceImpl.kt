package com.example.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.example.domain.datasource.FusedLocationDataSource
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks

class FusedLocationDataSourceImpl(context: Context) : FusedLocationDataSource {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLocation(): Location? {
        return Tasks.await(fusedLocationProviderClient.lastLocation) ?: null
    }
}