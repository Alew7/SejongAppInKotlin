package com.example.sejongapp.retrofitAPI.api2

import com.example.sejongapp.models.DataClass2.GroupsResponse
import retrofit2.http.GET

interface GroupsApi {
    @GET("mobile/get-groups")
    suspend fun getGroups(): GroupsResponse
}
