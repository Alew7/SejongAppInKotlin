package com.example.sejongapp.models.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProgramUpdate(
    val title: Title,
    val content: Content,
    val version: String
): Parcelable
