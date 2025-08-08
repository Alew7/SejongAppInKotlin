package com.example.sejongapp.retrofitAPI

import com.example.sejongapp.models.DataClasses.ScheduleData


sealed class NetworkResponse<out T> : Collection<ScheduleData> {
    data class Success<out T>(val data: T): NetworkResponse<T>()
    data class Error(val message: String): NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
    object Idle : NetworkResponse<Nothing>() /// добавил это

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<ScheduleData> {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<ScheduleData>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: ScheduleData): Boolean {
        TODO("Not yet implemented")
    }

}