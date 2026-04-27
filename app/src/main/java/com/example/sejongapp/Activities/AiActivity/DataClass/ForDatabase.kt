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


data class OpenAiRequest(
    val model: String = "gpt-5.4-nano",
    val messages: List<OpenAiMessage>,
    val stream: Boolean = false,

)

data class OpenAiMessage(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val choices: List<OpenAiChoice>
)

data class OpenAiChoice(
    val message: OpenAiMessage
)
