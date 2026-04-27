package com.example.sejongapp.Activities.AiActivity.AiViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.Activities.AiActivity.DataClass.Message
import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AIChatViewModel(private val repository: AiRepository) : ViewModel() {

    val messages = mutableStateListOf<Message>()
    val historyMessages = mutableStateListOf<Message>()
    var isLoading by mutableStateOf(false)
    var currentChatId by mutableStateOf("")
        private set

    private val gson = Gson()

    companion object {
        private var isFirstAppLaunch = true
    }

    private fun getUserCacheKey(context: Context): String {
        val token = LocalData.getSavedToken(context) ?: "guest"
        val uniqueId = if (token.length > 10) token.takeLast(10) else token.hashCode().toString()
        return "cached_history_$uniqueId"
    }

    fun startNewChat() {
        messages.clear()
        currentChatId = UUID.randomUUID().toString()
    }

    fun loadHistory(context: Context) {
        val token = LocalData.getSavedToken(context) ?: return
        val cacheKey = getUserCacheKey(context)
        val prefs = context.getSharedPreferences("ai_prefs", Context.MODE_PRIVATE)

        // 1. ЗАГРУЗКА ИЗ КЭША (МГНОВЕННО)
        val cached = prefs.getString(cacheKey, null)
        if (cached != null) {
            try {
                val type = object : TypeToken<List<Message>>() {}.type
                val saved: List<Message> = gson.fromJson(cached, type)

                if (saved.isNotEmpty()) {
                    historyMessages.clear()
                    historyMessages.addAll(saved)

                    // Если мы только зашли и экран пустой — восстанавливаем последний чат
                    if (messages.isEmpty()) {
                        val lastId = saved.first().chatID
                        currentChatId = lastId ?: ""
                        val lastChatMessages = saved.filter { it.chatID == lastId }.reversed()
                        messages.clear()
                        messages.addAll(lastChatMessages)
                    }
                }
            } catch (e: Exception) {
                Log.e("AI_DEBUG", "Кэш пуст или ошибка: ${e.message}")
            }
        }

        // 2. ЗАГРУЗКА ИЗ СЕТИ (ОБНОВЛЕНИЕ)
        viewModelScope.launch {
            try {
                val history = repository.fetchHistory(token)
                if (history.isNotEmpty()) {
                    val fetched = mutableListOf<Message>()
                    history.forEach { chat ->
                        val id = chat.chatId ?: UUID.randomUUID().toString()
                        val chatTitle = chat.title ?: "Чат"
                        val rawTime = chat.time ?: ""
                        val shortTime = if (rawTime.length >= 16) rawTime.substring(11, 16) else rawTime

                        chat.messages?.forEach { msg ->
                            // Важно: в historyMessages храним в обратном порядке (новые сверху)
                            fetched.add(0, Message(msg.answer, true, shortTime, id, chatTitle))
                            fetched.add(0, Message(msg.question, false, shortTime, id, chatTitle))
                        }
                    }

                    historyMessages.clear()
                    historyMessages.addAll(fetched)


                    prefs.edit().putString(cacheKey, gson.toJson(fetched)).apply()

                    // Если сообщений на экране всё еще нет (первый запуск), берем из того что пришло
                    if (messages.isEmpty() && fetched.isNotEmpty()) {
                        val firstId = fetched.first().chatID
                        openHistoryChat(firstId)
                    }
                }
            } catch (e: Exception) {
                Log.e("AI_DEBUG", "Ошибка сети: ${e.localizedMessage}")
            }
        }
    }

    fun saveCurrentMessagesCach(context: Context) {
        val cacheKey = getUserCacheKey(context)
        val prefs = context.getSharedPreferences("ai_prefs", Context.MODE_PRIVATE)
        val json = gson.toJson(historyMessages.toList())
        prefs.edit().putString(cacheKey, json).apply()
    }

    fun sendMessage(userText: String, context: Context) {
        if (userText.isBlank()) return
        if (currentChatId.isEmpty()) currentChatId = UUID.randomUUID().toString()

        val token = LocalData.getSavedToken(context) ?: ""
        val newChatTitle = if (userText.length > 25) userText.take(25) + "..." else userText

        val userMsg = Message(userText, false, getCurrentTime(), currentChatId, newChatTitle)
        messages.add(userMsg)
        historyMessages.add(0, userMsg)
        saveCurrentMessagesCach(context)

        isLoading = true

        viewModelScope.launch {
            try {
                // ТЕПЕРЬ ИСПОЛЬЗУЕМ DEEPSEEK ВМЕСТО GEMINI
                val aiResponse = OpenAiService.getResponse(userText)
                isLoading = false

                val aiMsg = Message(aiResponse, true, getCurrentTime(), currentChatId, newChatTitle)
                messages.add(aiMsg)
                historyMessages.add(0, aiMsg)
                saveCurrentMessagesCach(context)

                repository.saveToCloud(token, currentChatId, newChatTitle, userText, aiResponse)
            } catch (e: Exception) {
                isLoading = false
                messages.add(Message("Ошибка: ${e.localizedMessage}", true, getCurrentTime(), currentChatId, newChatTitle))
            }
        }
    }

    fun prepareAiContext(userData: UserData, scheduleList: List<ScheduleData>) {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val currentDayInt = (dayOfWeek + 5) % 7
        val currentDayName = SimpleDateFormat("EEEE", Locale("ru")).format(Date())

        val userGroupName = userData.groups.firstOrNull()
            ?.replace("[", "")?.replace("]", "")?.trim()?.uppercase() ?: ""

        val studentSchedule = scheduleList.find { it.group.trim().uppercase() == userGroupName }
        val todayLessons = studentSchedule?.time?.filter { it.day == currentDayInt }

        val scheduleContext = if (todayLessons.isNullOrEmpty()) {
            "На сегодня ($currentDayName) уроков нет."
        } else {
            "Расписание на сегодня ($currentDayName) для группы ${studentSchedule?.group}:\n" +
                    todayLessons.joinToString("\n") { "• ${it.start_time} - ${it.end_time}, кабинет ${it.classroom}" }
        }

        val systemPrompt = """
            Ты — Али ИИ, официальный помощник корейского центра Седжон в Душанбе 😎.
            Твоя специализация: корейский язык (K-Language), культура Кореи и помощь студентам.
            
            Правила:
            1. Имя студента: ${userData.fullname}.
            2. Отвечай кратко, вежливо и только по делу (максимум 2-3 предложения).
            3. Если студент просит перевод — делай его точным и пиши транскрипцию.
            4. Твои знания о расписании: $scheduleContext.
            5. Если вопрос не касается Кореи, центра Седжон или учебы — вежливо скажи, что ты помогаешь только с этими темами.
        """.trimIndent()

        //
        OpenAiService.currenSystemInstruction = systemPrompt
    }

    private fun getCurrentTime(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    fun openHistoryChat(selectedChatId: String?) {
        if (selectedChatId.isNullOrBlank()) return
        messages.clear()
        currentChatId = selectedChatId
        val chatContent = historyMessages.filter { it.chatID == selectedChatId }.reversed()
        messages.addAll(chatContent)
    }

    override fun onCleared() {
        super.onCleared()
        messages.clear()
        historyMessages.clear()
    }
}