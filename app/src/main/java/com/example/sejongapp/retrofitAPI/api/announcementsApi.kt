package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.AnnouncementData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface announcementsApi {
    @GET ("announcements/")
    suspend fun getAnnouncements(
        @Header("token") token: String
    ): Response<AnnouncementData>
}
