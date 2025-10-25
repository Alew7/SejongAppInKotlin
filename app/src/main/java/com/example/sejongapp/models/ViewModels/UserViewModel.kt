package com.example.sejongapp.models.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.ChangeUserData
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

    private val _userChangeResult = MutableLiveData<NetworkResponse<tokenData>>()
    val userChangeResult : LiveData<NetworkResponse<tokenData>> = _userChangeResult


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
        Log.i(TAG, "getUserData: trying to get user data. Token is ${token}")
        _userDataResult.value = Loading

        viewModelScope.launch {


            try {
                val response = userApi.getUserData(token)
                if (response.isSuccessful){
                    Log.i(TAG, "getUserData: data successfully taken " + response.body().toString())


                    response.body()?.let {
                        _userDataResult.value = Success(it)
                    }
                } else {
                    response.body()?.let {
                        _userDataResult.value = Error(response.message().toString())
                    }
                    Log.i(TAG, "getUserData: the response is not successful the response is ${response.message()}")
                    Log.e(TAG, response.message().toString())
                }
            }
            catch (e: Exception){
                _userTokenResult.value = Error(e.message.toString())
                Log.e(TAG, "getUserData: Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }
    }


    fun changeUserData(token: String, changeUserData: ChangeUserData){
        Log.i(TAG, "ChangeUserData: trying to change user data")
        _userChangeResult.value = Loading

        viewModelScope.launch {

            try {
                val response = userApi.changeUserData(token, changeUserData)
                if (response.isSuccessful){
                    Log.i(TAG, "ChangeUserData: data successfully changed " + response.body().toString())



                    response.body()?.let {
                        _userChangeResult.value = Success(it)
                    }
                } else {
                    response.body()?.let {
                        Log.e(TAG, "ChangeUserData failed! Error is ${response.message()}")
                        _userChangeResult.value = Error(response.message().toString())
                    }

                    Log.i(TAG, "ChangeUserData: the response is not successful, Couldn't change the user data. the response is ${response.message()}")
                    Log.e(TAG, response.message().toString())
                }
            }
            catch (e: Exception){
                _userChangeResult.value = Error(e.message.toString())
                Log.e(TAG, "ChangeUserData: Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }

    }

    fun resetUserResult(){
        _userTokenResult.value = Idle
    }

}
