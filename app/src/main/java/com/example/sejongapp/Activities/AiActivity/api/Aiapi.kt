package com.example.sejongapp.Activities.AiActivity.api

import com.example.sejongapp.Activities.AiActivity.DataClass.ChatHistoryResponse
import com.example.sejongapp.Activities.AiActivity.DataClass.ForDatabase
import com.example.sejongapp.Activities.AiActivity.DataClass.OpenAiRequest
import com.example.sejongapp.Activities.AiActivity.DataClass.OpenAiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Aiapi {
    @POST("gemini/save/")
    suspend fun saveChat (
        @Header("token") token: String,
        @Body chatData: ForDatabase
    ): Response<ForDatabase>



    @GET ("gemini/history/")
    suspend fun getHistory (
         @Header("token") token : String
    ): List<ChatHistoryResponse>

    interface OpenAiApi {
        @POST("v1/chat/completions")
        suspend fun getChatCompletion(
            @Header("Authorization") token: String,
            @Body request: OpenAiRequest
        ): OpenAiResponse
    }
}