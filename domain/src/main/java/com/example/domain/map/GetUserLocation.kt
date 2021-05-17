package com.example.domain.map

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface GetUserLocation {
    fun execute(): Flow<Result<Location?>>
}