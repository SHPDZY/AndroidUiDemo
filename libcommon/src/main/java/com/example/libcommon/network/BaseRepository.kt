package com.example.libcommon.network


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

open class BaseRepository {
//123123312312332
    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Response<T> {
        return call.invoke()
    }


    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Result<T>,
        errorMessage: String
    ): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <T : Any> executeResponse(
        response: Response<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): Result<T> {
        return coroutineScope {
            when (response.code) {
                0 -> {
                    successBlock?.let { it() }
                    Result.Success(response.data)
                }
                else -> {
                    errorBlock?.let { it() }
                    Result.Error(IOException(response.message)).apply {
                        errorCode = response.code
                        msg = response.message
                    }
                }
            }
        }
    }


}