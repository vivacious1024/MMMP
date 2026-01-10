package com.kk.mmmp.screens

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kk.mmmp.R
import com.kk.mmmp.runcode.ViewModelPython
import com.kk.mmmp.runcode.copyFileFromAssetsToInternalStorage
import com.kk.mmmp.runcode.filePicker
import java.io.IOException
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutingScreen(navController: NavController, viewModelPython: ViewModelPython, operation: String) {
    val context = LocalContext.current
    val fileUri = filePicker()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$operation 算法配置") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 模型描述区域
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(bottom = 16.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "算法说明",
                            style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 18.sp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ModelDescription(modelName = operation)
                    }
                }
            }

            // 文件选择状态
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = if (fileUri != null) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (fileUri != null) "已选择文件:" else "尚未选择数据文件",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    if (fileUri != null) {
                        Text(
                            text = fileUri.path ?: "未知路径",
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (fileUri != null) {
                        when (operation) {
                            "AHP" -> viewModelPython.callPythonCodeAHP(fileUri)
                            "EWM" -> viewModelPython.callPythonCodeEWM(fileUri)
                            "LOG" -> viewModelPython.callPythonCodeLOG(fileUri)
                            "TOPSIS" -> viewModelPython.callPythonCodeTOPSIS(fileUri)
                            "GREY" -> viewModelPython.callPythonCodeGREY(fileUri)
                            else -> Toast.makeText(context, "未知的操作类型", Toast.LENGTH_SHORT).show()
                        }
                        navController.navigate("resultScreen")
                    } else {
                        Toast.makeText(context, "请先选择 Excel 数据文件", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text(text = "开始运行算法", fontSize = 18.sp)
            }
        }
    }
}

//这个函数将不同方法的描述呈现出来
@Composable
fun ModelDescription(modelName: String) {
    val resourceMap = remember {
        mapOf(
            "AHP" to R.raw.ahp,
            "EWM" to R.raw.ewm,
            "LOG" to R.raw.logistic
        )
    }
    val resourceId = resourceMap[modelName]
    if (resourceId != null) {
        val context = LocalContext.current
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val text = String(buffer, Charsets.UTF_8)
        Text(text)
    } else {
        Text("(相关描述还在准备中……)")
        Log.e("ModelDescription", "File not found: $modelName")
    }
}