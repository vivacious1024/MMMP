package com.kk.mmmp.login

import com.kk.mmmp.database.User
import com.kk.mmmp.database.UserDao

class UserRepository(val userDao: UserDao) {
    //注册用户的逻辑
    suspend fun registerUser(username: String, password: String): Boolean{
        println("调用了repository.registerUser()")
        val user = userDao.getUserByUsername(username)
        println(user)

        //若数据库中有存在的用户就不行
        return if (user != null){
            false
        } else {    //没有就可以
            userDao.insertUser(User(username = username, password = password))
            true
        }
    }

    suspend fun getAllUser(): List<User>{
        return userDao.getAllUsers()
    }

}