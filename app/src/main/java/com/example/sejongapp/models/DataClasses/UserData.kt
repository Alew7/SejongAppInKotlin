package com.example.sejongapp.models.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserData(
    val username: String,
    val avatar: String,
    val fullname: String,
    val email: String,
    val status: String,
    val groups: List<String>
): Parcelable
