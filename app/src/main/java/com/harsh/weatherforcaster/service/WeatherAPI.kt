package com.harsh.weatherforcaster.service

import com.harsh.weatherforcaster.Model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("v1/forecast.json?key=b8582280ff2c4cb89fa171114222302&&days=1&aqi=no&alerts=no")
    fun getWeather(@Query("q") city:String): Call<Weather>

}