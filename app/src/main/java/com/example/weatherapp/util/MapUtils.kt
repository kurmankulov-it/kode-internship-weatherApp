package com.example.weatherapp.util


import android.view.LayoutInflater
import android.widget.*
import com.example.weatherapp.R
import com.example.weatherapp.presentation.ui.forecast.ForecastFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


fun GoogleMap.addPin(location: LatLng) {
    addMarker(
        MarkerOptions()
            .position(location)
            .title(location.toString())
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
            .anchor(0.5f, 0.5f)
    )
}

fun getPopupWindow(inflater: LayoutInflater): PopupWindow {
    val popupWindow = PopupWindow(
        inflater.inflate(R.layout.pop_up_weather_window, null),
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
    )
    popupWindow.contentView.findViewById<ImageView>(R.id.closeButton).setOnClickListener {
        popupWindow.dismiss()
    }

    return popupWindow
}
