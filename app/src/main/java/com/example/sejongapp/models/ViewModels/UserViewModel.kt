package com.example.sejongapp.models.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.UserData
import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.tokenData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.NetworkResponse.*
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {


    private val userApi =  RetrofitInstance.userApi


    private val _userTokenResult = MutableLiveData<NetworkResponse<tokenData>>()
    val userTokenResult : LiveData<NetworkResponse<tokenData>> = _userTokenResult


    private val _userDataResult = MutableLiveData<NetworkResponse<UserData>>()
    val userDataResult : LiveData<NetworkResponse<UserData>> = _userDataResult

    companion object {
        private const val TAG = "UserViewModel_TAG"
    }

    fun login(username: String, password: String){
        Log.i(TAG, "the username and password is $username - $password")

        _userTokenResult.value = Loading

        viewModelScope.launch {

            try {
                Log.i(TAG, "try to fetch to")
                val response = userApi.loginUser(loginRequestData(username, password))

                if (response.isSuccessful){
                    Log.i(TAG, "data successfully taken " + response.body().toString())

                    response.body()?.let {
                        _userTokenResult.value = Success(it)
                    }
                } else {
                    Log.e(TAG, response.message().toString())
                    _userTokenResult.value = Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e(TAG, e.message.toString())
                _userTokenResult.value = Error("Exception: ${e.message}")
            }
        }
    }



    fun getUserData(token: String){
        Log.i(TAG, "trying to get user data")
        _userDataResult.value = Loading

        viewModelScope.launch {


            try {
                val response = userApi.getUserData(token)
                if (response.isSuccessful){
                    Log.i(TAG, "data successfully taken " + response.body().toString())


                    response.body()?.let {
                        _userDataResult.value = Success(it)
                    }
                } else {

                    Log.i(TAG, "the response is not successful the response is ${response.message()}")
                    Log.e(TAG, response.message().toString())
                }
            }
            catch (e: Exception){
                Log.e(TAG, "Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun changeUserData(token: String, userData: UserData){
        Log.i(TAG, "trying to change user data")
        _userDataResult.value = Loading

        viewModelScope.launch {

            try {
                val response = userApi.changeUserData(token, userData)
                if (response.isSuccessful){
                    Log.i(TAG, "data successfully changed " + response.body().toString())


                    response.body()?.let {
                        _userDataResult.value = Success(it)
                    }
                } else {

                    Log.i(TAG, "the response is not successful, Couldn;t change the user data. the response is ${response.message()}")
                    Log.e(TAG, response.message().toString())
                }
            }
            catch (e: Exception){
                Log.e(TAG, "Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }

    }

    fun resetUserResult(){
        _userTokenResult.value = Idle
    }

}
