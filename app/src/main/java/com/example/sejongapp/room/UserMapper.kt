package com.example.sejongapp.room

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        username = username,
        fullname = fullname,
        email = email,
        avatar = avatar,
        status = status
    )
}