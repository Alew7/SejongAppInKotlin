package com.example.sejongapp.models.ViewModels2

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClass2.GroupSchedule
import com.example.sejongapp.models.DataClass2.Student
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MagazineViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_SELECTED_DATE = "selected_date"
    }

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    private val _availableDates = MutableStateFlow<List<String>>(emptyList())

    private val _selectedDate =
        MutableStateFlow(savedStateHandle[KEY_SELECTED_DATE] ?: "")

    val students: StateFlow<List<Student>> = _students
    val availableDates = _availableDates.asStateFlow()
    val selectedData: StateFlow<String> = _selectedDate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadGroupData(groupId: Int) {
        if (_students.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getGroupDetail(groupId)

                _students.value = response.data.group_students
                parseSchedule(response.data.group_schedule)

            } catch (e: Exception) {
                Log.e("MagazineVM", e.message ?: "error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseSchedule(schedule: GroupSchedule) {
        val result = mutableListOf<String>()

        schedule.days.forEach { (year, months) ->
            months.forEach { (month, days) ->
                days.forEach { day ->
                    result.add("$day $month $year")
                }
            }
        }

        _availableDates.value = result


        val currentSelected = _selectedDate.value

        if (currentSelected.isEmpty() && result.isNotEmpty()) {

            updateSelectedDate(result.first())
        } else if (currentSelected.isNotEmpty() && !result.contains(currentSelected)) {

            updateSelectedDate(result.first())
        }
    }

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
        savedStateHandle[KEY_SELECTED_DATE] = date
    }
}