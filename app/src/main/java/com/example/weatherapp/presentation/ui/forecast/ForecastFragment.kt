package com.example.weatherapp.presentation.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.domain.model.WeatherCondition
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModel()

    companion object {
        fun newInstance(cityName: String) = ForecastFragment().apply {
            arguments = Bundle().apply {
                putString("cityName", cityName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.title = arguments?.getString("cityName", "Forecast")

        val cityName = arguments?.getString("cityName", "")
        if (!cityName.isNullOrEmpty()) {
            viewModel.getWeather(cityName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ForecastFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            forecastViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.noWeatherInfoButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.weather.observe(viewLifecycleOwner, { weather ->
            if (weather != null) {
                binding.progressBar.visibility = View.GONE
                binding.weatherInfoLayout.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(weather.icon)
                    .into(binding.weatherIcon)
                binding.weatherImage.setImageResource(getImageResource(weather.condition))
            } else {
                binding.weatherInfoLayout.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.noWeatherInfoLayout.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    private fun getImageResource(condition: WeatherCondition?): Int {
        return when (condition) {
            WeatherCondition.CLEAR_SKY -> R.drawable.weather_clear_sky
            WeatherCondition.FEW_CLOUDS -> R.drawable.weather_few_clouds
            WeatherCondition.SCATTERED_CLOUDS -> R.drawable.weather_scattered_clouds
            WeatherCondition.BROKEN_CLOUDS -> R.drawable.weather_broken_clouds
            WeatherCondition.SHOWER_RAIN -> R.drawable.weather_shower_rain
            WeatherCondition.RAIN -> R.drawable.weather_rain
            WeatherCondition.THUNDERSTORM -> R.drawable.weather_thunderstorm
            WeatherCondition.SNOW -> R.drawable.weather_snow
            WeatherCondition.MIST -> R.drawable.weather_mist
            WeatherCondition.DEFAULT -> R.drawable.weather_clear_sky
            null -> R.drawable.weather_clear_sky
        }
    }
}