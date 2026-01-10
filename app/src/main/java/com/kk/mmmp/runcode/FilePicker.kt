package com.kk.mmmp.runcode

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun filePicker() : Uri?{
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    // 创建一个启动器，用于处理文件选择结果
    val startFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        fileUri = uri // 用户选择的文件 URI
    }

    Column {
        Button(onClick = {
            // 启动文件选择器，可以指定你想要选择的文件类型
            startFilePicker.launch(arrayOf("application/*"))
        }) {
            Text("选择文件")
        }

        // 显示选定文件的 URI
        Text("文件 URI: $fileUri")
    }
    return fileUri
}