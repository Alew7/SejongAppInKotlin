package com.example.sejongapp.models.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.ScheduleData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class ScheduleViewModel: ViewModel() {
    companion object {
        private const val TAG = "ScheduleViewModel_TAG"
    }



    private val scheduleApi =  RetrofitInstance.scheduleApi

    private val _scheduleResult = MutableLiveData<NetworkResponse<ArrayList<ScheduleData>>>()
    val scheduleResult : MutableLiveData<NetworkResponse<ArrayList<ScheduleData>>> = _scheduleResult


    fun getAllSchedules(){
        Log.i(TAG, "trying to fetch all schedules data")
        _scheduleResult.value = NetworkResponse.Loading

        viewModelScope.launch {


            try {
                val response = scheduleApi.getSchedules()
                if (response.isSuccessful) {
                    Log.i(TAG, "data successfully taken " + response.body().toString())
                    _scheduleResult.value = NetworkResponse.Success(response.body()!!)
                    Log.i(TAG, "data successfully added to value " + response.body().toString())

                }
                else {
                        Log.e(TAG, response.message().toString())
                        _scheduleResult.value = NetworkResponse.Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e(TAG, e.message.toString())
                _scheduleResult.value = NetworkResponse.Error("Exception: ${e.message}")

            }
        }
    }

    fun resetScheduleResult(){
        _scheduleResult.value = NetworkResponse.Idle
    }
}