package com.example.sejongapp.retrofitAPI.gradeBookapi

import com.example.sejongapp.models.DataClasses.StudentGroups.GroupDetailResponse
import com.example.sejongapp.models.DataClasses.StudentGroups.TeachersGroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface GroupsApi {

    @GET("mobile/get-groups")
    suspend fun getGroups(
        @Header("token") token: String
    ): Response<TeachersGroupResponse>

    @GET("mobile/get-group-data/{id}")
    suspend fun getGroupDetail(
        @Header("token") token: String,
        @Path("id") groupId: Int
    ): Response<GroupDetailResponse>

}
