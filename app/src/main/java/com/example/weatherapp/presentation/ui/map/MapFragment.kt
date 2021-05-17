package com.example.weatherapp.presentation.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
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
import com.example.weatherapp.util.addPin
import com.example.weatherapp.util.getPopupWindow
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

        popupWindow = getPopupWindow(layoutInflater)
        popupWindow.contentView.findViewById<Button>(R.id.showWeatherButton)
            .setOnClickListener(popUpWindowClickCallBack())

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        binding.mapProgressBar.visibility = View.GONE

        getUserLocationWithPermission()
        viewModel.userLocation.observe(viewLifecycleOwner, { location ->
            userLocationHandler(location)
        })

        viewModel.currentCity.observe(viewLifecycleOwner, { address ->
            currentCityHandler(address)
        })

        mMap.setOnMapClickListener { location ->
            mapClickHandler(location)
        }
    }

    private fun getUserLocationWithPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            viewModel.getUserLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun popUpWindowClickCallBack(): View.OnClickListener {
        return View.OnClickListener {
            popupWindow.dismiss()
            mMap.clear()
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_container, ForecastFragment.newInstance(
                    popupWindow.contentView.findViewById<TextView>(R.id.cityName).text.toString()
                )
            ).addToBackStack(null).commit()
        }
    }

    private fun mapClickHandler(location: LatLng) {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
        viewModel.getCurrentCity(location)
        mMap.clear()
        mMap.addPin(location)
    }

    private fun userLocationHandler(location: Location?) {
        if (location != null) {
            val coordinates = LatLng(location.latitude, location.longitude)
            viewModel.getCurrentCity(coordinates)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))
            mMap.addPin(coordinates)
        }
    }

    private fun currentCityHandler(address: Address?) {
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
                String.format(getString(R.string.lat_lng), lat, lon)
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 24)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.city_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}