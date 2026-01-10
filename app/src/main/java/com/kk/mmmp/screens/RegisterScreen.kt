package com.kk.mmmp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kk.mmmp.login.TopSnackbarHost
import com.kk.mmmp.login.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // 确认密码
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        snackbarHost = { TopSnackbarHost(hostState = snackbarHostState) {snackbardata ->
            Snackbar(snackbarData = snackbardata, modifier = Modifier.padding(16.dp))
        } },
        topBar = {
            TopAppBar(title = { Text("Register") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.padding(innerPadding)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = {
                        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "用户名或密码错误！！！",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            if (password == confirmPassword) {
                                scope.launch {
                                    userViewModel.registerUser(username, password) { successful ->
                                        if (successful) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "注册成功！",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                            navController.navigate("loginScreen")
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "注册失败：用户名已存在，再取一个吧",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "两次密码不匹配！",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }

                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("注 册")
                }
                Button(
                    onClick = {
                        navController.popBackStack() // 回到登录页面
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("返回登录")
                }
                Button(
                    onClick = { navController.navigate("allUsers") },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "查看所有用户")
                }
            }
        }
    }
}
