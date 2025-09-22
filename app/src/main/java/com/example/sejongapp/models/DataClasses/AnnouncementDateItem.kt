package com.example.sejongapp.models.DataClasses


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AnnouncementDateItem(
    val author: String,
    val content: Content,
    val custom_id: Int,
    val images: List<String>,
    val is_active: Boolean,
    val time_posted: String,
    val title: Title
) :Parcelable