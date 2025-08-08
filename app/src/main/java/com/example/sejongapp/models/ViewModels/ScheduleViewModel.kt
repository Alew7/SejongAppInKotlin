package com.example.sejongapp.models.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.models.DataClasses.tokenData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.NetworkResponse.Error
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class ScheduleViewModel: ViewModel() {
    companion object {
        private const val TAG = "ScheduleViewModel_TAG"
    }


    private val scheduleApi =  RetrofitInstance.scheduleApi

    private val _scheduleResult = MutableLiveData<NetworkResponse<ArrayList<ScheduleData>>>()
    val scheduleResult : MutableLiveData<NetworkResponse<ArrayList<ScheduleData>>> = _scheduleResult


    fun getAllSchedules(token: String){
        Log.i(TAG, "trying to fetch all schedules data")
        Log.i(TAG, "the token is $token")
        _scheduleResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            val response = scheduleApi.getSchedules(tokenData(token))

            try {
                if (response.isSuccessful) {
                    Log.i(TAG, "data successfully taken " + response.body().toString())

                    response.body()?.let {
                        _scheduleResult.value = NetworkResponse.Success(it)
                        Log.i(TAG, "data successfully added to value " + it.toString())
                    }
                }
                else {
                    Log.e(TAG, response.message().toString())
                    _scheduleResult.value = Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e(TAG, e.message.toString())
                _scheduleResult.value = Error("Exception: ${e.message}")

            }
        }
    }
}