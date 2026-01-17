package com.example.sejongapp.models.ViewModels.UserViewModels

import LocalData
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.ElectronicBookData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class ELibraryViewModel: ViewModel() {

    companion object {
        private const val TAG = "Library_TAG"
    }



    private val eLibAPI = RetrofitInstance.eLibApi

    private val _libResult = MutableLiveData<NetworkResponse<ArrayList<ElectronicBookData>>>()
    val libResult : MutableLiveData<NetworkResponse<ArrayList<ElectronicBookData>>> = _libResult


    fun getAllBooks(context: Context){
        Log.i(TAG, "Fetching all books from the server")
        _libResult.value = NetworkResponse.Loading

        val my_token = LocalData.getSavedToken(context)

        viewModelScope.launch {

            try{

                val response = eLibAPI.getBooks(my_token)

                if (response.isSuccessful){
                    Log.i(TAG, "all data books successfully taken" + response.body().toString())
                    _libResult.value = NetworkResponse.Success(response.body()!!)

                    Log.i(TAG, "data successfully added to value ")
                }
                else{
                    Log.e(TAG, response.message().toString())
                    _libResult.value = NetworkResponse.Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e(TAG, e.message.toString())
                _libResult.value = NetworkResponse.Error("Exception: ${e.message}")
            }
        }
    }


    fun resetLibResult(){
        _libResult.value = NetworkResponse.Idle
    }

}