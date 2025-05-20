package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.retrofitAPI.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseUrl = ""

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val userApi: UserApi = getInstance().create(UserApi::class.java)

}