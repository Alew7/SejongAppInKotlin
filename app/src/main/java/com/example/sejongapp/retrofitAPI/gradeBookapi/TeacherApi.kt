package com.example.sejongapp.retrofitAPI.gradeBookapi

import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.models.DataClasses.loginRequestData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TeacherApi {
    @POST("mobile/login")
    suspend fun loginTeacher(
        @Body request: loginRequestData
    ): Response<tokenData>
}