package com.example.sejongapp.models.ViewModels

import LocalData
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.ProgramUpdate
import com.example.sejongapp.models.DataClasses.ProgramUpdateData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class ProgramUpdateViewModel : ViewModel() {

    companion object {
        private const val TAG = "ProgramUpdateViewModel_TAG"
    }

    private val programUpdateApi = RetrofitInstance.ProgramupdateApi

    private val _programUpdate = MutableLiveData<NetworkResponse<ProgramUpdateData>>()
    val programUpdate: MutableLiveData<NetworkResponse<ProgramUpdateData>> = _programUpdate


    fun getProgramUpdate(context: Context) {
        Log.i(TAG, "Trying to fetch program update info")
        _programUpdate.value = NetworkResponse.Loading

        val myToken = LocalData.getSavedToken(context)

        viewModelScope.launch {
            try {
                val response = programUpdateApi.getProgramUpdate(myToken)

                if (response.isSuccessful) {
                    Log.i(TAG, "Program update received: ${response.body()}")
                    _programUpdate.value = NetworkResponse.Success(response.body()!!)
                } else {

                    Log.e(TAG, "Got the error: ${response}")
                    Log.e(TAG, "Error message: ${response.message()}")
                    _programUpdate.value = NetworkResponse.Error("Failed to fetch program update")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _programUpdate.value = NetworkResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun resetProgramUpdate() {
        _programUpdate.value = NetworkResponse.Idle
    }
}


