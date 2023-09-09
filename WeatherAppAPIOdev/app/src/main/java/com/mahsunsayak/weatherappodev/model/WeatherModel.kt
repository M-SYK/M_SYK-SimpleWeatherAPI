package com.mahsunsayak.weatherappodev.model

//Data class representing the response from the weather API, containing the count and a list of weather data.
data class WeatherResponse(
    val count: Int,         //Number of weather data entries in the response
    val data: List<WeatherModel> //List of weather data entries
)

// Data class representing individual weather data entry.
data class WeatherModel(
    val app_temp: String,
    val country_code: String,
    val city_name: String
) {
}
