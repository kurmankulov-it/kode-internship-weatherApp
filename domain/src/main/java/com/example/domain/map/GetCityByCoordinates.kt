package com.example.domain.map

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface GetCityByCoordinates {
    fun execute(location: LatLng): Flow<Result<Address?>>
}