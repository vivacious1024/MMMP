package com.kk.mmmp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//定义数据实体
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val username:String,
    val password:String
)