package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.ScheduleData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface ScheduleApi {
    @GET("schedules/")
    suspend fun getSchedules(
        @Header("token") token: String
    ): Response<ArrayList<ScheduleData>>

}