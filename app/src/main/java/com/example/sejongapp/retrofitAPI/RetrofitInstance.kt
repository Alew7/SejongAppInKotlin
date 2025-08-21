package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.retrofitAPI.api.ELibAPI
import com.example.sejongapp.retrofitAPI.api.ScheduleApi
import com.example.sejongapp.retrofitAPI.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseUrl = "https://sejong-backend.serveo.net/api/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val userApi: UserApi = getInstance().create(UserApi::class.java)
    val scheduleApi: ScheduleApi = getInstance().create(ScheduleApi::class.java)
    val eLibApi: ELibAPI = getInstance().create(ELibAPI::class.java)

}