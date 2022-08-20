package com.example.weatherapp.services

import com.example.weatherapp.model.WeatherModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    //http://api.openweathermap.org/data/2.5/weather?q=delhi&APPID=5737d899c1f50de85d6ae6541925d3a8

    @GET("data/2.5/weather?&units=metric&APPID=5737d899c1f50de85d6ae6541925d3a8")
    suspend fun getDataAsync(
        @Query("q")  cityName: String
    ) : WeatherModel

}