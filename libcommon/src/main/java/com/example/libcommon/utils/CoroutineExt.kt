package com.example.libcommon.utils

import kotlinx.coroutines.*

fun launch(onIoThread: suspend () -> Unit, onUiThread: (() -> Unit)? = null) {
    GlobalScope.launch(Dispatchers.Default) {
        try {
            withContext(Dispatchers.IO) {
                onIoThread()
            }
            withContext(Dispatchers.Main) {
                onUiThread?.invoke()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun launchDelay(timeMillis: Long = 100, onUiThread: (() -> Unit)? = null) {
    GlobalScope.launch(Dispatchers.Default) {
        try {
            withContext(Dispatchers.IO) {
                delay(timeMillis)
            }
            withContext(Dispatchers.Main) {
                onUiThread?.invoke()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun CoroutineScope.launchDelay(timeMillis: Long = 100, onUiThread: (() -> Unit)? = null) {
    launch(Dispatchers.Default) {
        try {
            withContext(Dispatchers.IO) {
                delay(timeMillis)
            }
            withContext(Dispatchers.Main) {
                onUiThread?.invoke()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}