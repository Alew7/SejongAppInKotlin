package com.example.sejongapp.retrofitAPI.api


import com.example.sejongapp.models.DataClasses.ChangeUserData
import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("auth/token/login/")
    suspend fun loginUser(
        @Body request: loginRequestData
    ): Response<tokenData>


    @GET("profile/")
    suspend fun getUserData(
        @Header("token") token: String
    ): Response<UserData>



    @POST("change_info/")
    suspend fun changeUserData(
        @Header("token") token: String,
        @Body request: ChangeUserData,
    ):Response<Any>
}