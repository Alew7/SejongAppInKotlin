package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.ElectronicBookData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface ELibAPI {
    @GET("elibrary/")
    suspend fun getBooks(
        @Header("token") token: String
    ): Response<ArrayList<ElectronicBookData>>
}