package com.example.zyuidemo.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

data class Response<out T>(val code: Int, val message: String, val timestamp: Long, val data: T)

suspend fun <T : Any> Response<T>.executeResponse(
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null,
): Result<T> {
    return coroutineScope {
        when (code) {
            0 -> {
                successBlock?.let { it() }
                Result.Success(data)
            }

            else -> {
                errorBlock?.let { it() }
                Result.Error(IOException(message)).apply {
                    errorCode = code
                    msg = message
                }
            }
        }
    }
}

suspend fun <T : Any> Response<T>.doSuccess(successBlock: (suspend CoroutineScope.(T) -> Unit)? = null): Response<T> {
    return coroutineScope {
        if (code == 0) {
            successBlock?.invoke(this, this@doSuccess.data)
        }
        this@doSuccess
    }
}

suspend fun <T : Any> Response<T>.doError(errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null): Response<T> {

    return coroutineScope {
        if (code != 0) {
            errorBlock?.invoke(this, this@doError.message)
        }
        this@doError
    }
}
