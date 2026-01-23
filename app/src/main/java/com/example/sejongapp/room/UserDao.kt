package com.example.sejongapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query ("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int





}