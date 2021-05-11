package com.example.weatherapp.app.koin

import android.location.Geocoder
import com.example.data.net.OpenWeatherApi
import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.forecast.GetWeatherByCityNameUseCase
import com.example.domain.map.GetCityByCoordinatesUseCase
import com.example.domain.map.GetUserLocationUseCase
import com.example.domain.repository.WeatherRepository
import com.example.weatherapp.presentation.ui.forecast.ForecastViewModel
import com.example.weatherapp.presentation.ui.map.MapViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl(
            openWeatherApi = get(),
            fusedLocationProviderClient = get(),
            geocoder = get()
        )
    }
}

val useCaseModule = module {
    single {
        GetWeatherByCityNameUseCase(weatherRepository = get())
    }

    single {
        GetUserLocationUseCase(weatherRepository = get())
    }

    single {
        GetCityByCoordinatesUseCase(weatherRepository = get())
    }
}

val locationModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }
    single {
        Geocoder(androidContext())
    }
}

private val retrofit: OpenWeatherApi = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(GsonConverterFactory.create())
    .build().create(OpenWeatherApi::class.java)

val networkModule = module {
    single {
        retrofit
    }
}

val viewModelModule = module {
    viewModel { ForecastViewModel(getWeatherByCityNameUseCase = get()) }
    viewModel { MapViewModel(getUserLocationUseCase = get(), getCityByCoordinatesUseCase = get()) }
}