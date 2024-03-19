package com.jiachian.nbatoday.common

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String? = null) : Resource<T>()
    class Loading<T> : Resource<T>()
}
