package com.example.sejongapp.models.ViewModels2

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClass2.GroupSchedule
import com.example.sejongapp.models.DataClass2.Student
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MagazineViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("magazine_prefs", Context.MODE_PRIVATE)

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _availableDates = MutableStateFlow<List<String>>(emptyList())
    val availableDates = _availableDates.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedData: StateFlow<String> = _selectedDate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Переменная для хранения текущего ID группы
    private var currentGroupId: Int = -1

    fun loadGroupData(groupId: Int) {
        currentGroupId = groupId // Запоминаем ID группы

        // 1. Сначала загружаем дату именно для ЭТОЙ группы из памяти
        val savedDate = prefs.getString("date_group_$groupId", "") ?: ""
        _selectedDate.value = savedDate

        if (_students.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getGroupDetail(groupId)
                _students.value = response.data.group_students
                parseSchedule(response.data.group_schedule)
            } catch (e: Exception) {
                Log.e("MagazineVM", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseSchedule(schedule: GroupSchedule) {
        val result = mutableListOf<String>()
        schedule.days.forEach { (year, months) ->
            months.forEach { (month, days) ->
                days.forEach { day -> result.add("$day $month $year") }
            }
        }
        _availableDates.value = result

        //  Если для этой группы еще нет сохраненной даты, ставим первую из расписания
        if (_selectedDate.value.isEmpty() && result.isNotEmpty()) {
            updateSelectedDate(result.first())
        }
    }

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date


        if (currentGroupId != -1) {
            prefs.edit().putString("date_group_$currentGroupId", date).apply()
        }
    }
}