package com.example.sejongapp.models.ViewModels.GradeBookViewModels

import LocalData
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.StudentGroups.Group
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GroupsViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMyGroups(context: Context) {
        val userData = LocalData.getUserData(context)
        val myNameKr = userData?.fullname ?: ""
        val isAdmin = userData?.status == UserStatusEnum.ADMIN

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.groupsApi.getGroups()

                val finalGroups = if (isAdmin) {
                    response.groups
                } else {
                    response.groups.filter { it.teacher_name_kr.trim() == myNameKr.trim() }
                }


                _groups.value = finalGroups

            } catch (e: Exception) {
                Log.e("GroupsVM", "Error: ${e.message}")
                _groups.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}