package com.example.sejongapp.retrofitAPI.gradeBookapi

import com.example.sejongapp.models.DataClasses.StudentGroups.TeachersGroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface GroupsApi {

    @GET("mobile/get-groups")
    suspend fun getGroups(
        @Header("token") token: String
    ): Response<TeachersGroupResponse>
}
