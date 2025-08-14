package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.ElectronicBookData
import retrofit2.Response
import retrofit2.http.GET


interface ELibAPI {
    @GET("elibrary/")
    suspend fun getBooks(): Response<ArrayList<ElectronicBookData>>
}