package com.jiachian.nbatoday.common

sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val message: String? = null) : Response<T>()

    fun onSuccess(invoke: (T) -> Unit) {
        if (this is Success) {
            invoke(data)
        }
    }

    fun onError(invoke: (String?) -> Unit) {
        if (this is Error) {
            invoke(message)
        }
    }
}
