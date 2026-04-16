package com.example.sejongapp.Activities.AiActivity.DataClass

import com.google.gson.annotations.SerializedName

data class Message(
    val text: String,
    val isFromAI: Boolean,
    val time: String,
    val chatID: String,
    val title: String
)

data class ChatHistoryResponse(
    @SerializedName("chat_id") val chatId: String?,
    val title: String?,
    val time: String?,
    val messages: List<ChatMessageItem>?
)

data class ChatMessageItem(
    val question: String,
    val answer: String
)

data class ForDatabase(
    val chat_id: String,
    val title: String,
    val question: String,
    val answer: String
)

data class DeepSeekRequest(
    val model: String = "deepseek-chat",
    val messages: List<DeepSeekMessage>,
    val stream: Boolean = false
)

data class DeepSeekMessage(
    val role: String,
    val content: String
)

data class DeepSeekResponse(
    val choices: List<DeepSeekChoice>
)

data class DeepSeekChoice(
    val message: DeepSeekMessage
)
