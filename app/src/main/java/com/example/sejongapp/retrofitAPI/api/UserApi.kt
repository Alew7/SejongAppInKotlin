package com.example.sejongapp.retrofitAPI.api


import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserAvatarInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    suspend fun changeUserName(
        @Header("token") token: String,
        @Body request: ChangeUserInfo,
    ):Response<ChangeUserInfo>

    @POST("change_info/")
    suspend fun changeUserPassword(
        @Header("token") token: String,
        @Body request: ChangeUserPassword,
    ):Response<tokenData>

    @Multipart
    @POST("change_avatar/")
    suspend fun changeUserAvatar(
        @Header("token") token: String,
        @Part new_avatar: MultipartBody.Part
    ): Response<ChangeUserAvatarInfo>

}