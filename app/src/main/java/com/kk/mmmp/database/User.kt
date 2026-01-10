package com.kk.mmmp.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

//定义数据实体
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val username:String,
    val password:String
)

//创建数据访问对象
@Dao
interface UserDao{
    @Insert
    suspend fun insertUser(user: User)//这里不能写代码，不然就是override了

    @Query("select * from user where username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("select * from user")
    suspend fun getAllUsers(): List<User>
}

//创建一个Room数据库实例
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}

//不允许同名用户存在