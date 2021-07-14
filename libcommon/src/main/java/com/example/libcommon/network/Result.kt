package com.example.libcommon.network

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>() {
        var errorCode: Int = -1
        var msg: String = exception.message ?: ""
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]  errorCode=$errorCode  msg=$msg"
        }
    }
}