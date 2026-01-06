package com.example.sejongapp.retrofitAPI.api2

import com.example.sejongapp.models.DataClass2.GroupDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SejongApiService {

    @GET("mobile/get-group-data/{id}")
    suspend fun getGroupDetail(
        @Path("id") groupId: Int
    ): GroupDetailResponse
}