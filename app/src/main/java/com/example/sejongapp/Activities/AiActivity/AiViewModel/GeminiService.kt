package com.example.sejongapp.Activities.AiActivity.AiViewModel

import com.example.sejongapp.Activities.AiActivity.DataClass.OpenAiMessage
import com.example.sejongapp.Activities.AiActivity.DataClass.OpenAiRequest
import com.example.sejongapp.Activities.AiActivity.api.Aiapi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object OpenAiService {
    private const val BASE_URL = "https://api.openai.com/"
    private const val API_KEY = "" // Вставь свой ключ сюда

    var currenSystemInstruction: String = "Ты — помощник студента."

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(Aiapi.OpenAiApi::class.java)

    suspend fun getResponse(userText: String): String {
        val request = OpenAiRequest(
            model="gpt-5.4-nano",
            messages = listOf(
                OpenAiMessage(role = "system", content = currenSystemInstruction),
                OpenAiMessage(role = "user", content = userText)
            ),

        )

        return try {
            val response = api.getChatCompletion("Bearer $API_KEY", request)
            response.choices.firstOrNull()?.message?.content ?: "Нет ответа"
        } catch (e: Exception) {
            val errorMsg = e.message ?: ""
            when {
                errorMsg.contains("429") -> "Лимит запросов или закончились деньги на OpenAI."
                errorMsg.contains("401") -> "Ошибка: Неверный API ключ OpenAI."
                else -> "Ошибка OpenAI: ${e.localizedMessage}"
            }
        }
    }
}




