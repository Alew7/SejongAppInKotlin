package com.example.sejongapp.retrofitAPI.gradeBookapi

import com.example.sejongapp.models.DataClasses.StudentGroups.GroupDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SejongApiService {

    @GET("mobile/get-group-data/{id}")
    suspend fun getGroupDetail(
        @Path("id") groupId: Int
    ): GroupDetailResponse
}