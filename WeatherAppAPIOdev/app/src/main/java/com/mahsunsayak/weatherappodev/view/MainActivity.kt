package com.mahsunsayak.weatherappodev.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.mahsunsayak.weatherappodev.databinding.ActivityMainBinding
import com.mahsunsayak.weatherappodev.model.WeatherModel
import com.mahsunsayak.weatherappodev.model.WeatherResponse
import com.mahsunsayak.weatherappodev.service.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.weatherbit.io/v2.0/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //https://api.weatherbit.io/v2.0/current?lat=39.905813760193&lon=41.26454250644382&key=36bb7a25f5784ba6be5d136a5f6b0e1e&include=hour

        //Load weather data on app startup
        loadData()

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Handle refresh action, reload data by calling `loadData()`
            // Örneğin, verileri yeniden çekmek için `loadData()` işlevini çağır.
            loadData()
        }

        val imgSearchCity = binding.imgSearchCity
        imgSearchCity.setOnClickListener {
            val cityName = binding.enterCityName.text.toString()
            if (cityName.isEmpty()) {
                //Show an alert dialog if the city name is empty
                showAlertDialog("Please enter region name.")
            } else if (cityName != "Yakutiye") {
                //Show an alert dialog if the entered region name is not "Yakutiye"
                showAlertDialog("The data of the region you entered does not exist in the system.")
            } else {
                //If the region name is valid, reload the data
                loadData()
            }
        }
    }

    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()//.create(WeatherAPI::class.java)

        val service = retrofit.create(WeatherAPI::class.java)
        val call = service.getData(39.905813760193, 41.26454250644382, "36bb7a25f5784ba6be5d136a5f6b0e1e", "minutely")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        val weatherModel = weatherResponse.data[0] //We take the first data, because we only need one data
                        binding.cityName.text = weatherModel.city_name
                        binding.countryCode.text = weatherModel.country_code
                        binding.cityDegree.text = "${weatherModel.app_temp}°C"
                    }
                }
                // Indicate the refresh operation is complete when data is successfully fetched or in case of an error
                binding.swipeRefreshLayout.isRefreshing = false
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
                //Indicate the refresh operation is complete in case of an error
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error!")
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}