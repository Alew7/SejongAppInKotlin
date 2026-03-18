package com.example.sejongapp.Activities.AiActivity.AiViewModel

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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AIChatViewModel : ViewModel() {

    val messages = mutableStateListOf<Message>()
    var isLoading by mutableStateOf(false)
        private set

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return
        messages.add(Message(text = userText, isFromAI = false, time = getCurrentTime()))
        isLoading = true

        viewModelScope.launch {
            try {
                val aiResponse = GeminiService.getResponse(userText)
                isLoading = false
                messages.add(Message(text = aiResponse, isFromAI = true, time = getCurrentTime()))
            } catch (e: Exception) {
                isLoading = false
                messages.add(Message(text = "Ошибка: ${e.localizedMessage}", isFromAI = true, time = getCurrentTime()))
            }
        }
    }

    fun prepareAiContext(userData: UserData, scheduleList: List<ScheduleData>) {

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)


        val currentDayInt = (dayOfWeek + 5) % 7
        Log.d("AI_DEBUG", "Current day: $currentDayInt")
        val currentDayName = SimpleDateFormat("EEEE", Locale("ru")).format(Date())

        val userGroupName = userData.groups.firstOrNull()
            ?.replace("[", "")
            ?.replace("]","")
            ?.trim()
            ?.uppercase() ?: ""


        Log.d("AI_DEBUG", "=== START PREPARE CONTEXT ===")
        Log.d("AI_DEBUG", "User Group: '$userGroupName'")
        Log.d("AI_DEBUG", "Today is: $currentDayName (Index: $currentDayInt)")


        val studentSchedule = scheduleList.find {
            it.group.trim().uppercase() == userGroupName
        }

        if (studentSchedule == null) {
            Log.e("AI_DEBUG", "ERROR: Группа '$userGroupName' не найдена в списке расписаний!")
            Log.d("AI_DEBUG", "Available groups: ${scheduleList.map { it.group }}")
        }

        studentSchedule?.time?.forEach {
            Log.d("AI_DEBUG", "В базе есть урок: День=${it.day}, время${it.start_time}")
        }
        val todayLessons = studentSchedule?.time?.filter { it.day == currentDayInt }
        Log.d("AI_DEBUG", "Lessons found today: ${todayLessons?.size ?: 0}")


        val scheduleContext = if (todayLessons.isNullOrEmpty()) {
            "На сегодня ($currentDayName) уроков по расписанию нет."
        } else {
            "Расписание на сегодня ($currentDayName) для группы ${studentSchedule?.group}:\n" +
                    todayLessons.joinToString("\n") { "• ${it.start_time} - ${it.end_time}, кабинет ${it.classroom}" }
        }

        val systemPrompt = """
    Ты — Али ИИ, стильный помощник центра Седжон. 
    Твоя задача — отвечать четко, красиво и с уважением.

    СТУДЕНТ: ${userData.fullname}
    ГРУППА: ${studentSchedule?.group ?: userGroupName}

    РАСПИСАНИЕ НА СЕГОДНЯ:
    $scheduleContext

    ПРАВИЛА ОФОРМЛЕНИЯ (ОБЯЗАТЕЛЬНО):
    1. Используй заголовок: ✨ Твоё расписание на сегодня ✨
    2. Время и кабинет выделяй жирным: 14:00 и Кабинет 307.
    3. Информацию об уроке пиши с новой строки через эмодзи 📍 и ⏰.
    4. В конце добавь короткое пожелание на корейском, например: "Удачи! (Haiting! ✊)" или "Хорошего дня! 😊".

    ПРИМЕР:
    Привет, ${userData.fullname}! 👋
    Вот твои занятия:

    📍 307 кабинет
    ⏰ 14:00 - 15:30

    Удачи на учебе! 회이팅! 🇰🇷
""".trimIndent()

        GeminiService.currenSystemInstruction = systemPrompt
        Log.d("AI_DEBUG", "System Prompt updated successfully!")
    }

    private fun getCurrentTime(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}