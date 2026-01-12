package com.kk.mmmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kk.mmmp.login.UserRepository
import com.kk.mmmp.screens.LoginScreen
import com.kk.mmmp.screens.RegisterScreen
import com.kk.mmmp.login.UserViewModel
import com.kk.mmmp.screens.MainScreen
import com.kk.mmmp.ui.theme.MMMPTheme
import com.kk.mmmp.runcode.PythonRepository
import com.kk.mmmp.runcode.ViewModelFactory
import com.kk.mmmp.runcode.ViewModelPython
import com.kk.mmmp.screens.AllUsersScreen
import com.kk.mmmp.screens.ErrorScreen
import com.kk.mmmp.screens.ExecutingScreen
import com.kk.mmmp.screens.ResultScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MMMPTheme {
                AppNavigation()
            }
        }
    }
}
@Composable
fun AppNavigation(){
    //首先是用户登录需要的数据库访问和viewmodel
    val userDao = MyApp.db.userDao()
    val userViewModel = UserViewModel(UserRepository(userDao))

    //然后是运行python代码所需要的viewmodel
    val viewModelPython : ViewModelPython = viewModel(
        factory = ViewModelFactory(PythonRepository(LocalContext.current))
    )

    val navController = rememberNavController()     //创建并记住一个NavController实例

    NavHost(navController, startDestination = "loginScreen"){       //第二个参数是出发地
        composable("loginScreen"){ LoginScreen(navController, userViewModel) }   //第一条路
        composable("registerScreen"){ RegisterScreen(navController, userViewModel) }    //第二条路
        composable("allUsers"){ AllUsersScreen(navController, userViewModel) }
        composable("mainScreen"){ MainScreen(navController) }       //到主界面了

        composable("executingScreenAHP"){ ExecutingScreen(navController, viewModelPython, "AHP") }   //到executingScreen时执行ExecutingScreen()
        composable("executingScreenEWM"){ ExecutingScreen(navController, viewModelPython, "EWM") }
        composable("executingScreenLOG"){ ExecutingScreen(navController, viewModelPython, "LOG") }
        composable("executingScreenTOPSIS"){ ExecutingScreen(navController, viewModelPython, "TOPSIS") }
        composable("executingScreenGREY"){ ExecutingScreen(navController, viewModelPython, "GREY") }
        composable("executingScreenMissingValue"){ ExecutingScreen(navController, viewModelPython, "MISSING") }
        composable("executingScreenOutlier"){ ExecutingScreen(navController, viewModelPython, "OUTLIER") }
        composable("executingScreenCorrelation"){ ExecutingScreen(navController, viewModelPython, "CORR") }
        composable("executingScreenKMeans"){ ExecutingScreen(navController, viewModelPython, "KMEANS") }
        composable("executingScreenLinearReg"){ ExecutingScreen(navController, viewModelPython, "LINREG") }
        composable("ERROR"){ ErrorScreen(navController) }

        composable("resultScreen"){ ResultScreen(viewModelPython, navController) }                        //到resultScreen时执行ResultScreen()

    }   //它是一个导航图，提供了导航的可能性和目的地，但不会自己执行；而navController.navigate()才是具体触发，两者协同工作，共同构成了导航体系
}