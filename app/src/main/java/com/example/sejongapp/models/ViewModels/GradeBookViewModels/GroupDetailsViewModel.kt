package com.example.sejongapp.models.ViewModels.GradeBookViewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.StudentGroups.Group
import com.example.sejongapp.models.DataClasses.StudentGroups.GroupDataWrapper
import com.example.sejongapp.models.DataClasses.StudentGroups.GroupDetailResponse
import com.example.sejongapp.models.DataClasses.StudentGroups.GroupSchedule
import com.example.sejongapp.models.DataClasses.StudentGroups.Student
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "GroupDetailsViewModel_TAG"

    private val prefs = application.getSharedPreferences("magazine_prefs", Context.MODE_PRIVATE)

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _availableDates = MutableStateFlow<List<String>>(emptyList())
    val availableDates = _availableDates.asStateFlow()

    val _data  = MutableStateFlow<NetworkResponse<GroupDetailResponse>>(
        NetworkResponse.Idle
    )
    val data : StateFlow<NetworkResponse<GroupDetailResponse>> = _data




    private val _selectedDate = MutableStateFlow("")
    val selectedData: StateFlow<String> = _selectedDate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Переменная для хранения текущего ID группы
    private var currentGroupId: Int = -1

    fun loadGroupData(groupId: Int, context: Context) {

        val token = LocalData.getSavedTeacherToken(context)

        Log.d(TAG, "loadGroupData() called with groupId: $groupId")

        currentGroupId = groupId


        // 1. Сначала загружаем дату именно для ЭТОЙ группы из памяти
        val savedDate = prefs.getString("date_group_$groupId", "") ?: ""
        _selectedDate.value = savedDate

        if (_students.value.isNotEmpty()) return

        viewModelScope.launch {
            Log.d(TAG, "Executing request to: mobile/get-group-data/$groupId")


            try {
                _isLoading.value = true
                _data.value = NetworkResponse.Loading
                Log.d(TAG, "Executing request to: mobile/get-group-data/$groupId")
                val response = RetrofitInstance.groupsApi.getGroupDetail(token, groupId)


                if (response.isSuccessful){
                    Log.i(TAG, "Success! Received ${response.body()?.message} groups")

                    val body = response.body()
                    if (body != null){
                        Log.i(TAG, "Success! the data is ${body.data} groups")
                        _data.value = NetworkResponse.Success(body)
                    }
                    else{
                        Log.e(TAG, "Error: ${response.code()} ${response.message()}")

                        _data.value = NetworkResponse.Error("Ответ сервера пуст")

                    }
                }
            }
            catch (e: Exception){

                Log.e(TAG, "Critical Network Error: ${e.message}")
                _data.value = NetworkResponse.Error("Ошибка соединения: ${e.localizedMessage}")

            }
            finally {
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