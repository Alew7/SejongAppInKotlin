package com.example.sejongapp.retrofitAPI.gradeBookapi

import com.example.sejongapp.models.DataClasses.StudentGroups.GroupsResponse
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface GroupsApi {

    @GET("mobile/get-groups")
    suspend fun getGroups(
        @Header("token") token: String
    ): GroupsResponse
}
