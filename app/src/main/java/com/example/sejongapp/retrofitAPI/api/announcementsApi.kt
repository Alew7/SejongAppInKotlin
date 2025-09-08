package com.example.sejongapp.retrofitAPI.api

import com.example.sejongapp.models.DataClasses.AnnouncementData
import retrofit2.Response
import retrofit2.http.GET

interface announcementsApi {
    @GET ("announcements/")
    suspend fun getAnnouncements(): Response<ArrayList<AnnouncementData>>
}
