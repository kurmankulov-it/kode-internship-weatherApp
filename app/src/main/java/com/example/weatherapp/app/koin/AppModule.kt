package com.example.weatherapp.app.koin

import com.example.data.datasource.FusedLocationDataSourceImpl
import com.example.data.datasource.GeocoderDataSourceImpl
import com.example.data.net.OpenWeatherApi
import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.datasource.FusedLocationDataSource
import com.example.domain.datasource.GeocoderDataSource
import com.example.domain.forecast.GetWeatherByCityName
import com.example.domain.forecast.GetWeatherByCityNameImpl
import com.example.domain.map.GetCityByCoordinates
import com.example.domain.map.GetCityByCoordinatesImpl
import com.example.domain.map.GetUserLocation
import com.example.domain.map.GetUserLocationImpl
import com.example.domain.repository.WeatherRepository
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.presentation.ui.forecast.ForecastViewModel
import com.example.weatherapp.presentation.ui.map.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl(
            openWeatherApi = get(),
            fusedLocationDataSource = get(),
            geocoderDataSource = get()
        )
    }
}

val useCaseModule = module {
    single<GetWeatherByCityName> {
        GetWeatherByCityNameImpl(weatherRepository = get())
    }

    single<GetUserLocation> {
        GetUserLocationImpl(weatherRepository = get())
    }

    single<GetCityByCoordinates> {
        GetCityByCoordinatesImpl(weatherRepository = get())
    }
}

val locationModule = module {
    single<FusedLocationDataSource> {
        FusedLocationDataSourceImpl(androidContext())
    }
    single<GeocoderDataSource> {
        GeocoderDataSourceImpl(androidContext())
    }
}

private val retrofit: OpenWeatherApi = Retrofit.Builder()
    .baseUrl(BuildConfig.API_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build().create(OpenWeatherApi::class.java)

val networkModule = module {
    single {
        retrofit
    }
}

val viewModelModule = module {
    viewModel { ForecastViewModel(getWeatherByCityName = get()) }
    viewModel { MapViewModel(getUserLocation = get(), getCityByCoordinates = get()) }
}