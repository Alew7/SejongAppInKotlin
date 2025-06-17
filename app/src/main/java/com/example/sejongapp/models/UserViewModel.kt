package com.example.sejongapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.NetworkResponse.*
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch



class UserViewModel: ViewModel() {


    private val userApi =  RetrofitInstance.userApi



    private val _userResult = MutableLiveData<NetworkResponse<tokenData>>()
    val userResult : LiveData<NetworkResponse<tokenData>> = _userResult


    companion object {
        private const val TAG = "UserViewModel_TAG"
    }

    fun login(username: String, password: String){
        Log.i(TAG, "the username and password is $username - $password")

        _userResult.value = Loading

        viewModelScope.launch {

            Log.i(TAG, "try to fetch to")
            val response = userApi.loginUser(loginRequestData(username, password))



            try {
                if (response.isSuccessful){
                    Log.i(TAG, "data successfully taken  " + response.body().toString())

                    if (response.body()!!.token == null) {
                        Log.i(TAG, "token is empty so it is an error  ")
                        _userResult.value = NetworkResponse.Error("Token is empty")
                    }
                    else{
                        response.body()?.let {
                            Log.i(TAG, "the token is full so it is fine")
                            _userResult.value = NetworkResponse.Success(it)
                        }
                    }


                } else {
                    Log.e(TAG, response.message().toString())
                    _userResult.value = Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e(TAG, e.message.toString())
                _userResult.value = NetworkResponse.Error("Exception: ${e.message}")
            }
        }
    }


    fun resetUserResult(){
        _userResult.value = Idle
    }
}
