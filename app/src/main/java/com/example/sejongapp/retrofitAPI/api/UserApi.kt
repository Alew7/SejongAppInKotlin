package com.example.sejongapp.retrofitAPI.api


import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("login/")
    suspend fun loginUser(
        @Body request: loginRequestData
    ): Response<tokenData>
}