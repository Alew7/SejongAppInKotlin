package com.example.sejongapp.retrofitAPI.api
import com.example.sejongapp.models.DataClasses.ProgramUpdateData
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Header

interface programupdateApi {
    @GET ("notice/")
    suspend fun getProgramUpdate(
        @Header("token") token: String
    ): Response<ProgramUpdateData>
}

