package com.example.sejongapp.models.DataClasses

import android.os.Parcel
import android.os.Parcelable

data class AnnouncementData_old(
    val author: String,
    val content: String,
    val custom_id: Int,
    val images: List<String>,
    val is_active: Boolean,
    val time_posted: String,
    val title: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeInt(custom_id)
        parcel.writeStringList(images)
        parcel.writeByte(if (is_active) 1.toByte() else 0.toByte())
        parcel.writeString(time_posted)
        parcel.writeString(title)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AnnouncementData_old> {
        override fun createFromParcel(parcel: Parcel): AnnouncementData_old {
            return AnnouncementData_old(parcel)
        }

        override fun newArray(size: Int): Array<AnnouncementData_old?> {
            return arrayOfNulls(size)
        }
    }
}
