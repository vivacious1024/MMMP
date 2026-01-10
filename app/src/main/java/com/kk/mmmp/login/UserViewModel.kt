package com.kk.mmmp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.mmmp.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun loginUser(username: String, password: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.userDao.getUserByUsername(username)
            //得返回主线程才能保证之后的navigation不出问题
            withContext(Dispatchers.Main) {
                onResult(user?.password == password)
            }
        }
    }

    // 用 LiveData 来观察注册状态
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> = _registrationStatus

    suspend fun registerUser(username: String, password: String, callback: (Boolean) -> Unit) {
        println("调用了viewmodel.registerUser()")

        // 实现用户注册逻辑，然后调用 callback 通知注册是否成功
        // 例如：callback(true) 表示成功，callback(false) 表示失败
        val result = repository.registerUser(username, password)
        _registrationStatus.value = result
        callback(result)

        println(result)
    }

    suspend fun getAllUser(): List<User> {
        return repository.getAllUser()
    }
}
