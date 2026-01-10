package com.kk.mmmp.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 一个可重用的函数来显示 Snackbar
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TopSnackbar(
    scope: CoroutineScope = rememberCoroutineScope(), // 使用 rememberCoroutineScope 获取默认的协程作用域
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }, // 记住状态以避免重复弹出
    message: String, // 传递给 Snackbar 的消息
    actionLabel: String? = null, // 可选的操作标签
    duration: SnackbarDuration = SnackbarDuration.Short, // 显示时长，默认短时长
    onActionClick: (() -> Unit)? = null // 可选的操作点击事件处理器
) {
    // 用来触发 Snackbar 显示的函数
    scope.launch {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        ).also { result ->
            // 如果用户执行了操作，则触发操作点击事件处理器
            if (result == SnackbarResult.ActionPerformed) {
                onActionClick?.invoke()
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun TopSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = hostState,
            snackbar = snackbar,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}