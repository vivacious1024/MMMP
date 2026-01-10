package com.kk.mmmp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun ShowDialogImage(onConfirm: () -> Unit, onDismiss: () -> Unit) {//目前是长按保存图片时会弹出的对话框
    // 对话框组件
    Dialog(onDismissRequest = onDismiss) { // 使用传入的lambda作为dismiss请求
        // 对话框内容
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("你想要保存这张图片到相册吗？")
                Spacer(modifier = Modifier.height(8.dp))
                Row{
                    Button(onClick = onConfirm) {
                        Text("确定")
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    Button(onClick = onDismiss) { // 使用传入的lambda关闭对话框
                        Text("取消")
                    }
                }
            }
        }
    }
}