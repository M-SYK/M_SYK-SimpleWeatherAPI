package com.mahsunsayak.weatherappodev.service

import com.mahsunsayak.weatherappodev.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//This interface defines the API endpoints and request parameters for weather data retrieval.
interface WeatherAPI {
    //Define a GET request to retrieve current weather data based on latitude, longitude, API key, and inclusion options.
    @GET("current")
    fun getData(
        @Query("lat") lat: Double, //Latitude coordinate
        @Query("lon") lon: Double, //Longitude coordinate
        @Query("key") apiKey: String, //API key for authentication
        @Query("include") include: String //Additional options (e.g., "hour")
    ): Call<WeatherResponse> //WeatherResponse should represent a class that corresponds to the JSON data returned from the API.
}
