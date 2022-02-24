package com.harsh.weatherforcaster.repository

import androidx.lifecycle.MutableLiveData
import com.harsh.weatherforcaster.Model.Weather
import com.harsh.weatherforcaster.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(_city: String) {


    val weatherLiveData: MutableLiveData<Weather> = MutableLiveData()


    private var city = _city

    init {
        getWeather()

    }

    fun getWeather() {
        ApiClient.getClient().getWeather(city).enqueue(
            object : Callback<Weather> {
                override fun onFailure(call: Call<Weather>, t: Throwable) {
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    try {
                        weatherLiveData.value = response.body()
                    } catch (e: Exception) {

                    }
                }
            }
        )
    }


}