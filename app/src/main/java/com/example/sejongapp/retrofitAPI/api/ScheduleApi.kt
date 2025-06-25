package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.DataClasses.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET


interface ScheduleApi {
    @GET("getSchedule/")
    suspend fun getSchedules(
        @Body token: tokenData
    ): Response<ArrayList<ScheduleData>>

}