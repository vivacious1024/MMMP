package com.kk.mmmp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from user where username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("select * from user")
    suspend fun getAllUsers(): List<User>
}
