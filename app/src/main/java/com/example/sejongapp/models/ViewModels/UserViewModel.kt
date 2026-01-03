package com.example.sejongapp.models.ViewModels



import LocalData.compressImageToTempFile
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserAvatarInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserInfo
import com.example.sejongapp.models.DataClasses.UserDataClasses.ChangeUserPassword
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserData
import com.example.sejongapp.models.DataClasses.UserDataClasses.UserDataDTO
import com.example.sejongapp.models.DataClasses.loginRequestData
import com.example.sejongapp.models.DataClasses.UserDataClasses.tokenData
import com.example.sejongapp.retrofitAPI.NetworkResponse
import com.example.sejongapp.retrofitAPI.NetworkResponse.*
import com.example.sejongapp.retrofitAPI.RetrofitInstance
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.coroutines.launch

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody



class UserViewModel: ViewModel() {


    private val userApi =  RetrofitInstance.userApi


    private val _userTokenResult = MutableLiveData<NetworkResponse<tokenData>>()
    val userTokenResult : LiveData<NetworkResponse<tokenData>> = _userTokenResult

    private val _userChangeResult = MutableLiveData<NetworkResponse<Any>>()
    val userChangeResult : LiveData<NetworkResponse<Any>> = _userChangeResult


    private val _userDataResult = MutableLiveData<NetworkResponse<UserData>>()
    val userDataResult : LiveData<NetworkResponse<UserData>> = _userDataResult


    private val _userAvatarResult = MutableLiveData<NetworkResponse<ChangeUserAvatarInfo>>()
    val userAvatarResult : LiveData<NetworkResponse<ChangeUserAvatarInfo>> = _userAvatarResult

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

    fun UserDataDTO.toUserData(): UserData {
        return UserData(
            username = this.username,
            avatar = this.avatar,
            fullname = this.fullname,
            email = this.email,
            groups = this.groups,
            status = when (this.status.uppercase()) { // .uppercase() для надежности
                "STUDENT" -> UserStatusEnum.STUDENT
                "TEACHER" -> UserStatusEnum.TEACHER
                "ADMIN" -> UserStatusEnum.ADMIN
                else -> UserStatusEnum.UNKNOWN // Важно иметь значение по умолчанию!
            }
        )
    }

    fun getUserData(token: String){
        Log.i(TAG, "getUserData: trying to get user data. Token is ${token}")
        _userDataResult.value = Loading

        viewModelScope.launch {


            try {
                val response = userApi.getUserData(token)
                if (response.isSuccessful){
                    Log.i(TAG, "getUserData: data successfully taken " + response.body().toString())


                    response.body()?.let {dto->
                        val fetchedDta: UserData = dto.toUserData()
                        _userDataResult.value = Success(fetchedDta)

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


    fun changeUserName(token: String, newUserData: ChangeUserInfo){
        Log.i(TAG, "changeUserName: trying to change user data")
        _userChangeResult.value = Loading

        viewModelScope.launch {

            Log.i(TAG, "changeUserName: Let's do it!")
            try {
                val response = userApi.changeUserName(token, newUserData)
                if (response.isSuccessful){
                    Log.i(TAG, "changeUserName: data successfully changed " + response.body().toString())
                    Log.i(TAG, "changeUserName: Only it's body " + response.body())


                    response.body()?.let {
                        _userChangeResult.value = Success(it)
                    }
                } else {
                    Log.v(TAG, "changeUserName: the response is not successful, Couldn't change the user data. the response is ${response.body()}")
                    Log.e(TAG, response.message().toString())
                    _userChangeResult.value = Error(response.message().toString())
                }
            }
            catch (e: Exception){
                _userChangeResult.value = Error(e.message.toString())
                Log.e(TAG, "ChangeUserData: Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }

    }


    fun changeUserPassword(token: String, newUserData: ChangeUserPassword){
        Log.i(TAG, "changeUserPassword: trying to change user data")
        _userChangeResult.value = Loading

        viewModelScope.launch {

            Log.i(TAG, "changeUserPassword: Let's do it!")
            try {
                val response = userApi.changeUserPassword(token, newUserData)
                if (response.isSuccessful){
                    Log.i(TAG, "changeUserPassword: data successfully changed " + response.body().toString())
                    Log.i(TAG, "changeUserPassword: Only it's body " + response.body())



                    response.body()?.let {
                        _userChangeResult.value = Success(it)
                    }
                } else {
                    Log.v(TAG, "changeUserPassword: the response is not successful, Couldn't change the user data. the response is ${response.body()}")
                    Log.e(TAG, response.message().toString())
                    _userChangeResult.value = Error(response.message().toString())
                }
            }
            catch (e: Exception){
                _userChangeResult.value = Error(e.message.toString())
                Log.e(TAG, "changeUserPassword: Some error occurred")
                Log.e(TAG, e.message.toString())
            }
        }

    }

    fun changeUserAvatar(context: Context, token: String, uri: Uri) {
        val TAG = "AvatarChange_TAG"

        _userAvatarResult.value = Loading


        val compressedFile = compressImageToTempFile(context, uri, 25)
        Log.i(TAG, "the image in Binary ${compressedFile}")
        val requestBody = compressedFile?.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            name = "new_avatar", // MUST match backend key
            filename = compressedFile?.name,
            body = requestBody!!
        )


        Log.i("AvatarChange_TAG", "Trying to change User Avatar")

        viewModelScope.launch {
            try {
                Log.i(TAG, "Sending the response!")
                val response = userApi.changeUserAvatar(token, new_avatar = imagePart)
                if (response.isSuccessful) {

                    Log.d(TAG, "Got the data $response")
                    val info = response.body()
                    Log.d(TAG, "Avatar updated: ${info}")
                    _userAvatarResult.value = Success(info!!)
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                    Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun resetUserResult(){
        _userTokenResult.value = Idle
    }
}