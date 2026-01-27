package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.retrofitAPI.api.ELibAPI
import com.example.sejongapp.retrofitAPI.api.ScheduleApi
import com.example.sejongapp.retrofitAPI.api.UserApi
import com.example.sejongapp.retrofitAPI.api.announcementsApi
import com.example.sejongapp.retrofitAPI.api.programupdateApi
import com.example.sejongapp.retrofitAPI.gradeBookapi.GroupsApi
import com.example.sejongapp.retrofitAPI.gradeBookapi.SejongApiService
import com.example.sejongapp.retrofitAPI.gradeBookapi.TeacherApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    private const val baseUrl = "https://sejong-app-container-785993649958.us-central1.run.app/api/"

    private const val digitalGradwbookUrl = "http://192.168.0.101:3000/"


    val api: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }




    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)   // time to connect to server
        .writeTimeout(120, TimeUnit.SECONDS)    // time to upload image
        .readTimeout(120, TimeUnit.SECONDS)     // time to wait for server response
        .build()



    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    private fun getGradeBookInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(digitalGradwbookUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // for 1st API
    val userApi: UserApi = getInstance().create(UserApi::class.java)
    val scheduleApi: ScheduleApi = getInstance().create(ScheduleApi::class.java)
    val eLibApi: ELibAPI = getInstance().create(ELibAPI::class.java)
    val AnnouncementsApi: announcementsApi = getInstance().create(announcementsApi::class.java)
    val ProgramupdateApi: programupdateApi = getInstance().create(programupdateApi::class.java)


    // 2 for 2st API

    val groupsApi: GroupsApi = getGradeBookInstance().create(GroupsApi::class.java)
    val teacherApi: TeacherApi = getGradeBookInstance().create(TeacherApi::class.java)
    val sejongApiService: SejongApiService = getGradeBookInstance().create(SejongApiService::class.java)
}