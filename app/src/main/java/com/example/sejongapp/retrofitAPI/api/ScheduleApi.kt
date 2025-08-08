package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.DataClasses.tokenData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ScheduleApi {
    @GET("getSchedule/")
    suspend fun getSchedules(
        @Query("token") token: tokenData
    ): Response<ArrayList<ScheduleData>>

}