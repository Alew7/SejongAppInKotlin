package com.example.sejongapp.retrofitAPI


import com.example.sejongapp.models.loginRequestData
import com.example.sejongapp.models.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("login/")
    suspend fun loginUser(
        @Body request: loginRequestData
    ): Response<tokenData>
}