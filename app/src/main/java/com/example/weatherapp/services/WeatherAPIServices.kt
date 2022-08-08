package com.example.weatherapp.services

import com.example.weatherapp.model.WeatherModel
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherAPIServices {

    //http://api.openweather.org/data/2.5/weather?q=bingo1&APPID=5737d899c1f50de85d6ae6541925d3a8

    private val baseUrl = "http://api.openweather.org/"

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    suspend fun getDataService(cityName: String): WeatherModel {
        return api.getDataAsync(cityName)
    }
}