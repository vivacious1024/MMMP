package com.kk.mmmp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kk.mmmp.database.User
import com.kk.mmmp.login.UserViewModel

@Composable
fun AllUsersScreen(navController: NavController, viewModel: UserViewModel){
    val usersState = produceState<List<User>>(initialValue = emptyList()) {
        value = viewModel.getAllUser()
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        LazyColumn {
            items(usersState.value){
                Text(text = "用户名：${it.username}")
            }

        }
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "返回")
        }
    }
}