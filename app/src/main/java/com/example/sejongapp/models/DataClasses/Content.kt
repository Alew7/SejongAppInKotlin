package com.example.sejongapp.models.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Content(
    val eng: String,
    val kor: String,
    val rus: String,
    val taj: String
) : Parcelable