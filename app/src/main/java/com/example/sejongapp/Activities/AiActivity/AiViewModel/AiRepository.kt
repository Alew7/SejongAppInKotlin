package com.example.sejongapp.Activities.AiActivity.AiViewModel


import android.util.Log
import com.example.sejongapp.Activities.AiActivity.DataClass.ChatHistoryResponse
import com.example.sejongapp.Activities.AiActivity.DataClass.ForDatabase
import com.example.sejongapp.Activities.AiActivity.api.Aiapi

class AiRepository (private val api: Aiapi) {

    suspend fun saveToCloud (token: String, ChatId: String, title: String, question: String, answer: String ) {
        try {
            val data = ForDatabase(
                chat_id = ChatId,
                title = title,
                question = question,
                answer = answer
            )
            api.saveChat(token, data)
        } catch (e: Exception) {
            Log.e ("API_ERROR","Не удалось сохронить: ${e.message}")
        }
    }

    suspend fun fetchHistory(token: String): List<ChatHistoryResponse> {
        return try {
            val response = api.getHistory(token)
            Log.d("AI_DEBUG", "История загружена: ${response.size} сообщений")
            response
        } catch (e: Exception) {
            Log.e("AI_DEBUG", "ОШИБКА ПРИ ЗАГРУЗКЕ: ${e.localizedMessage}")
            e.printStackTrace()
            emptyList()
        }
    }
}