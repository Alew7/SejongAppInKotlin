package com.example.sejongapp.models.ViewModels.GradeBookViewModels

import LocalData
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.StudentGroups.Group
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



const val TAG = "GroupsViewModel_TAG"
class GroupsViewModel : ViewModel() {
    private val getGroupsApi = RetrofitInstance.groupsApi

    private val _getGroups = MutableLiveData<NetworkResponse<List<Group>>>()
    val getGroup: MutableLiveData<NetworkResponse<List<Group>>> = _getGroups

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMyGroups(context: Context) {
        Log.d(TAG, "loadMyGroups() called")


        val token = LocalData.getSavedTeacherToken(context)
        if (token == "null" || token.isNullOrBlank()) {
            Log.e(TAG, "Token is null")
            _getGroups.value = NetworkResponse.Error("Token is null")

        }


        _getGroups.value = NetworkResponse.Loading
        _isLoading.value = true



        viewModelScope.launch {
            try {
                Log.d(TAG, "Executing request to: mobile/get-groups")
                Log.d(TAG, "Auth Header: $token")

                val response = getGroupsApi.getGroups(token)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Log.i(TAG, "Success! Received ${body.message} groups")
                        Log.i(TAG, "Success! the data is ${body.data} groups")
                        _groups.value = body.data
                        _getGroups.value = NetworkResponse.Success(body.data)
                    } else {
                        _groups.value = emptyList()
                        _getGroups.value = NetworkResponse.Error("Ответ сервера пуст")
                    }
                }
                else {
                    Log.e(TAG, "Error: ${response.code()} ${response.message()}")
                    Log.e(TAG, "Error Body: ${response.errorBody()?.string()}")
                }

                Log.d(TAG, "request is completed the response is ${response.body()}")
            } catch (e: Exception) {
                Log.e(TAG, "Critical Network Error: ${e.message}")
                _groups.value = emptyList()
                _getGroups.value = NetworkResponse.Error("Ошибка соединения: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}