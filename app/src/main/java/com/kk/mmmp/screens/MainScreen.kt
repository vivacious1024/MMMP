package com.kk.mmmp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数学建模方法软件包（Mathematical Modeling Methodology Package）") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                MyItem(navController, btnName = "1.评价类问题", subBtnName = listOf("层次分析法（AHP）", "熵权法", "灰色关联分析法", "TOPSIS模型", "模糊综合评价法", "神经网络算法", "蒙特卡罗模拟", "模拟退火"))
                MyItem(navController, btnName = "2.预测类问题", subBtnName = listOf("Logistic预测模型", "灰度预测模型", "二次指数平滑预测", "ARMA时间序列预测", "季节指数预测", "BP神经网络", "高斯回归预测", "马尔科夫预测", "回归分析预测", "蒙特卡罗模拟", "模拟退火"))
                MyItem(navController, btnName = "3.优化类问题", subBtnName = listOf("线性规划", "动态优化", "非线性规划", "多目标规划模型", "粒子群算法", "模拟退火", "遗传算法"))
                MyItem(navController, btnName = "4.数据预处理", subBtnName = listOf("缺失值处理", "异常值处理"))
                MyItem(navController, btnName = "5.相关性分析", subBtnName = listOf("卡方检验", "协方差"))
                MyItem(navController, btnName = "6.分类问题", subBtnName = listOf("K-Means算法", "层次聚类算法"))
                MyItem(navController, btnName = "7.图与网络", subBtnName = listOf("Dijkstra模型", "Floyd模型"))
            }
        }
    }
}

@Composable
fun MyItem(navController: NavController, btnName:String, subBtnName:List<String>){
    val expanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.LightGray)
            .clickable(onClick = {
                expanded.value = !expanded.value
            }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = btnName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd),
            tint = Color.DarkGray
        )
    }
    if (expanded.value) {
        Column(modifier = Modifier.padding(16.dp)) {
            subBtnName.forEach{sub->
                Button(
                    onClick = { navController.navigate(nameConversion(sub)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(sub)
                }
            }
        }
    }
}

//将中文名字转换为相应简写以方便代码运行
fun nameConversion(sub: String) : String {
    return when (sub) {
        "层次分析法（AHP）" -> {
            println("AHP clicked")
            "executingScreenAHP"
        }
        "熵权法" -> {
            println("熵权法 clicked")
            "executingScreenEWM"
        }
        "Logistic预测模型" -> {
            println("Logistic预测模型 clicked")
            "executingScreenLOG"
        }
        "TOPSIS模型" -> {
            "executingScreenTOPSIS"
        }
        "灰度预测模型" -> {
            "executingScreenGREY"
        }
        "缺失值处理" -> {
            "executingScreenMissingValue"
        }
        "异常值处理" -> {
            "executingScreenOutlier"
        }
        "卡方检验", "协方差" -> {
             "executingScreenCorrelation"
        }
        "K-Means算法" -> {
             "executingScreenKMeans"
        }
         "回归分析预测" -> {
             "executingScreenLinearReg"
         }
        else -> {
            println("Unknown sub button clicked: $sub")
            "ERROR"
        }
    }
}