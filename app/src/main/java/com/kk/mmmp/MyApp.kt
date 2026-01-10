package com.kk.mmmp

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.kk.mmmp.database.AppDatabase
import com.kk.mmmp.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set

        val db: AppDatabase by lazy {
            Room.databaseBuilder(
                instance.applicationContext,
                AppDatabase::class.java, "LoginDatabase"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.Default).launch {
                            instance.prepopulateData()
                        }
                    }
                })
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 由于db是惰性初始化，你并不需要在这里显式初始化它；
        // 它会在首次使用时自动完成初始化。
        // 下面是python环境的初始化
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }

    private suspend fun prepopulateData() {
        val userDao = db.userDao()
        userDao.insertUser(User(100000000, "root", "123456"))
    }
}
