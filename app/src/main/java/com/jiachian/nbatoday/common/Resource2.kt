package com.jiachian.nbatoday.common

sealed class Resource2<T> {
    data class Success<T>(val data: T) : Resource2<T>()
    data class Error<T>(val message: String? = null) : Resource2<T>()
    class Loading<T> : Resource2<T>()
}
