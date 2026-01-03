package com.example.sejongapp.models.DataClasses.UserDataClasses

import android.os.Parcelable
import com.example.sejongapp.utils.UserStatusEnum
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserData(
    val username: String,
    val avatar: String,
    val fullname: String,
    val email: String,
    val status: UserStatusEnum,
    val groups: List<String>
): Parcelable


@Parcelize
data class UserDataDTO(
    val username: String,
    val avatar: String,
    val fullname: String,
    val email: String,
    val status: String,
    val groups: List<String>
): Parcelable



