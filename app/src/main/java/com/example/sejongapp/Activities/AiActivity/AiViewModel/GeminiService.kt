package com.example.sejongapp.Activities.AiActivity.AiViewModel

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

object GeminiService {

    private const val API_KEY = "AIzaSyB0S03rDHI0RNIBXzRnRHe8aii6YtVSkSw"

    var currenSystemInstruction: String = "Ты — помощник студента."

    suspend fun getResponse(userMessage: String): String {
        return try {
            val model = GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = API_KEY,
                systemInstruction = content { text(currenSystemInstruction) }
            )

            val response = model.generateContent(userMessage)
            response.text ?: "Я не смог ответить"

        } catch (e: Exception) {

            val errorMsg = e.message ?: ""

            when {
                errorMsg.contains("429") -> "Слишком много запросов. Подожди немного."
                errorMsg.contains("Quota") -> "Достигнут лимит Gemini API."
                else -> "Ошибка связи: ${e.localizedMessage}"
            }
        }
    }
}




