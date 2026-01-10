package com.kk.mmmp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.kk.mmmp.view.ShowDialogImage
import com.kk.mmmp.runcode.saveImageToGallery
import com.kk.mmmp.runcode.ViewModelPython
import kotlinx.coroutines.launch
import java.io.File

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(viewModelPython: ViewModelPython, navController: androidx.navigation.NavController) {
    val imageFilePath by viewModelPython.imageFilePath.collectAsState()
    val message by viewModelPython.message.collectAsState()
    val resultData by viewModelPython.resultData.collectAsState()
    val context = LocalContext.current
    var showFullScreen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("数模计算分析报告") },
                actions = {
                    androidx.compose.material3.IconButton(onClick = { navController.navigate("mainScreen") {
                        popUpTo("mainScreen") { inclusive = true }
                    } }) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Home,
                            contentDescription = "回到首页"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            // 1. 可视化图表卡片
            item {
                if (imageFilePath != null) {
                    androidx.compose.material3.Card(
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "结果可视化",
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            val painter = rememberAsyncImagePainter(model = File(imageFilePath))
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onDoubleTap = { showFullScreen = true },
                                            onLongPress = { showDialog = true }
                                        )
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                "提示：双击放大，长按保存至相册",
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. 结论概要卡片
            item {
                androidx.compose.material3.Card(
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text(
                            "分析结论",
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = message ?: "计算完成，暂无摘要信息",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // 3. 详细数值数据卡片
            if (resultData.isNotEmpty()) {
                item {
                    androidx.compose.material3.Card(
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "详细数值结果",
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                            )
                            androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            resultData.forEachIndexed { index, value ->
                                androidx.compose.foundation.layout.Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                                ) {
                                    Text("参数/指标 [${index + 1}]", color = Color.Gray)
                                    Text(
                                        text = String.format("%.4f", value),
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 全屏显示逻辑
    if (showFullScreen && imageFilePath != null) {
        Dialog(onDismissRequest = { showFullScreen = false }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(Color.Black)
            ) {
                val painter = rememberAsyncImagePainter(model = File(imageFilePath))
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // 保存图片逻辑
    if (showDialog && imageFilePath != null) {
        ShowDialogImage(
            onConfirm = {
                coroutineScope.launch {
                    val saveResult = saveImageToGallery(context, imageFilePath!!)
                    Toast.makeText(context, saveResult.message, Toast.LENGTH_SHORT).show()
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}