package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.models.DataClasses.ScheduleData


sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T): NetworkResponse<T>()
    data class Error(val message: String): NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
    object Idle : NetworkResponse<Nothing>() /// добавил это

}