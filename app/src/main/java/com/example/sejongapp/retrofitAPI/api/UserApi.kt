package com.example.sejongapp.retrofitAPI.api


import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("login/")
    suspend fun loginUser(
        @Body request: loginRequestData
    ): Response<tokenData>


    @GET("profile/")
    suspend fun getUserData(
        @Query("token") token: String
    ): Response<UserData>
}