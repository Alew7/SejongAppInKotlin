package com.example.sejongapp.room

import com.example.sejongapp.retrofitAPI.api.UserApi

class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {

    suspend fun getUser(token: String): UserEntity {

        val count = dao.countUsers()
        if (count > 0) return dao.getUser()!!

        val response = api.getProfile(token)

        if (response.isSuccessful) {
            val body = response.body()!!

            val userEntity = UserEntity(
                username = body.username,
                fullname = body.fullname,
                email = body.email,
                avatar = body.avatar,
                status = body.status
            )

            dao.insertUser(userEntity)
            return dao.getUser()!!
        } else {
            throw Exception("Ошибка сервера: ${response.code()}")
        }
    }

    suspend fun logout() {
        dao.clearUsers()
    }
}
