package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.retrofitAPI.api.ELibAPI
import com.example.sejongapp.retrofitAPI.api.ScheduleApi
import com.example.sejongapp.retrofitAPI.api.UserApi
import com.example.sejongapp.retrofitAPI.api.announcementsApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val baseUrl = "https://sejong-app-container-785993649958.us-central1.run.app/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)   // time to connect to server
        .writeTimeout(120, TimeUnit.SECONDS)    // time to upload image
        .readTimeout(120, TimeUnit.SECONDS)     // time to wait for server response
        .build()

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val userApi: UserApi = getInstance().create(UserApi::class.java)
    val scheduleApi: ScheduleApi = getInstance().create(ScheduleApi::class.java)
    val eLibApi: ELibAPI = getInstance().create(ELibAPI::class.java)
    val AnnouncementsApi: announcementsApi = getInstance().create(announcementsApi::class.java)

}

// https://sejong-app-container-785993649958.us-central1.run.app/api/