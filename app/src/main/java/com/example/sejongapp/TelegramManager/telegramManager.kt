package com.example.sejongapp.TelegramManager

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

object TelegramManager {
    private val client = OkHttpClient()


    private const val BOT_TOKEN = "8249495388:AAH5O3H21dyuiR-sQ64UhugfAR-N4N5Y2KY"


    private const val MY_CHAT_ID = "5274055258"

    suspend fun sendReview(rating: Int, comment: String, userName: String,status: String,group: String) {
        withContext(Dispatchers.IO) {
            val stars = "⭐".repeat(rating)

            val messageText = """
                📩 <b>Новый отзыв!</b>
                👤 <b>Статус</b> $status
                📚 <b>Группа</b> $group
                👤 <b>От:</b> $userName
                🌟 <b>Оценка:</b> $stars ($rating/5)
                💬 <b>Текст:</b> $comment
            """.trimIndent()

            val encodeMsg = URLEncoder.encode(messageText,"UTF-8")


            val url = "https://api.telegram.org/bot$BOT_TOKEN/sendMessage" +
                    "?chat_id=$MY_CHAT_ID" +
                    "&text=$encodeMsg" +
                    "&parse_mode=HTML"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.d("TG_DEBUG", "Отправлено успешно!")
                    } else {
                        Log.e("TG_DEBUG", "Ошибка сервера: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                Log.e("TG_DEBUG", "Ошибка сети: ${e.message}")
            }
        }
    }
}