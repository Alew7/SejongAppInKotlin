package com.example.sejongapp.models.ViewModels2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClass2.Student
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MagazineViewModel : ViewModel() {
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadGroupData(groupId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                val response = RetrofitInstance.api.getGroupDetail(groupId)
                _students.value = response.data.group_students
            } catch (e: Exception) {
                Log.e("MagazineVM", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}