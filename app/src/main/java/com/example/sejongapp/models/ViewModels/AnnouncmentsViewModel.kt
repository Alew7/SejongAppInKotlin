package com.example.sejongapp.models.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.AnnouncementData

import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class AnnouncmentsViewModel : ViewModel() {
    companion object {
        private const val TAG = "AnnouncmentsViewModel_TAG"
    }

    private val announcmentApi = RetrofitInstance.AnnouncementsApi

    private val _announcments = MutableLiveData<NetworkResponse<AnnouncementData>>()
    val announcments: MutableLiveData<NetworkResponse<AnnouncementData>> = _announcments


    fun getAllannouncments(context: Context) {
        Log.i(TAG, "trying to fetch all announcments data")
        _announcments.value = NetworkResponse.Loading

        val my_token = LocalData.getSavedToken(context)

        viewModelScope.launch {

            try {

                val response = announcmentApi.getAnnouncements(my_token)

                if (response.isSuccessful) {
                    Log.i(TAG, "data successfully taken " + response.body().toString())
                    _announcments.value = NetworkResponse.Success(response.body()!!)
                    Log.i(TAG, "data successfully added to value " + response.body().toString())

                } else {
                    Log.e(TAG, response.message().toString())
                    _announcments.value = NetworkResponse.Error("Failed to fetch data")

                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                _announcments.value = NetworkResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun resetAnnouncments() {
        _announcments.value = NetworkResponse.Idle
    }
}




