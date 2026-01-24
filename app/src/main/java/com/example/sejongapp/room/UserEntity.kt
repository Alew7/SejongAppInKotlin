package com.example.sejongapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val username: String,
    val fullname: String,
    val email: String,
    val avatar: String?,
    val status: String

)
