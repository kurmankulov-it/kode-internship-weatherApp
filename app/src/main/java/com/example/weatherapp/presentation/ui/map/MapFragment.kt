package com.example.weatherapp.presentation.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastFragmentBinding
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.presentation.ui.forecast.ForecastFragment
import com.example.weatherapp.util.Constants.CHECK_INTERNET_MESSAGE
import com.example.weatherapp.util.MapUtils.addPin
import com.example.weatherapp.util.isConnected
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModel()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var mMap: GoogleMap

    private lateinit var popupWindow: PopupWindow

    private lateinit var binding: FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.getUserLocation()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        popupWindow = PopupWindow(
            layoutInflater.inflate(R.layout.pop_up_weather_window, null),
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        popupWindow.contentView.findViewById<Button>(R.id.showWeatherButton).setOnClickListener {
            popupWindow.dismiss()
            mMap.clear()
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_container, ForecastFragment.newInstance(
                    popupWindow.contentView.findViewById<TextView>(R.id.cityName).text.toString()
                )
            ).addToBackStack(null).commit()
        }
        popupWindow.contentView.findViewById<ImageView>(R.id.closeButton).setOnClickListener {
            popupWindow.dismiss()
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        binding.mapProgressBar.visibility = View.GONE

        getUserLocationWithPermission()
        viewModel.userLocation.observe(viewLifecycleOwner, { location ->
            when {
                location.isSuccess -> {
                    val userLocation = location.getOrNull()
                    if (userLocation != null) {
                        val coordinates = LatLng(userLocation.latitude, userLocation.longitude)
                        viewModel.getCurrentCity(coordinates)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))
                        mMap.addPin(coordinates)
                    }
                }
            }
        })

        viewModel.currentCity.observe(viewLifecycleOwner, { cityNameResult ->
            when {
                cityNameResult.isSuccess -> {
                    val address = cityNameResult.getOrNull()
                    if (!address?.locality.isNullOrEmpty()) {
                        popupWindow.contentView.findViewById<TextView>(R.id.cityName).text =
                            address?.locality
                        val lat =
                            address?.latitude?.let { Location.convert(it, Location.FORMAT_DEGREES) }
                        val lon = address?.longitude?.let {
                            Location.convert(
                                it,
                                Location.FORMAT_DEGREES
                            )
                        }
                        popupWindow.contentView.findViewById<TextView>(R.id.location).text =
                            "$lat $lon"
                        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 24)
                    }
                }
                cityNameResult.isFailure -> {
                    Toast.makeText(
                        requireContext(),
                        CHECK_INTERNET_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        mMap.setOnMapClickListener { location ->
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
            viewModel.getCurrentCity(location)
            mMap.clear()
            mMap.addPin(location)
        }
    }

    private fun getUserLocationWithPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                viewModel.getUserLocation()
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}