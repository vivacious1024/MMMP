package com.kk.mmmp.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kk.mmmp.R
import com.kk.mmmp.login.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel){
    var username by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()//获取协程的scope

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) {innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
            ){
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
            ) {
                Image(painterResource(id = R.drawable.mmmp1), contentDescription = null)
                OutlinedTextField(
                    value = username,
                    onValueChange = {username = it},
                    label = {Text("Username")},
                    singleLine = true,
                    //modifier = Modifier.padding(all = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = {Text("Password")},
                    singleLine = true,
                    //modifier = Modifier.padding(all = 8.dp)
                )
                Button(
                    onClick = {
                        userViewModel.loginUser(username, password){successful->
                            if (successful){
                                navController.navigate("mainScreen")
                                println("登录成功！！！")
                            }else{
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "登录失败：无效用户名或密码",
                                        actionLabel = "重试",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                println("无此用户。。。")
                            }
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(8.dp)
                ){
                    Text("登 录")
                }
                Button(
                    onClick = {
                        navController.navigate("registerScreen")
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("注 册")
                }
            }
        }

    }
}
