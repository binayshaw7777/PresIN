package com.geekymusketeers.presin.network

/**
 * Model for the API response. We get three callbacks {onSuccess, onError, onException} depending upon the response
 */
sealed class NetworkResponse<T> {
    class Success<T>(val data: T) : NetworkResponse<T>()
    class Error<T>(val error: ApiError) : NetworkResponse<T>()
    class Exception<T>(val throwable: Throwable) : NetworkResponse<T>()
}

suspend fun <T> NetworkResponse<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Success<T>) {
        executable(data)
    }
}

suspend fun <T> NetworkResponse<T>.onError(
    executable: suspend (error: ApiError) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Error<T>) {
        executable(error)
    }
}

suspend fun <T> NetworkResponse<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Exception<T>) {
        executable(throwable)
    }
}