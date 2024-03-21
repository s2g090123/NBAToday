package com.jiachian.nbatoday.common

private typealias RootError = Error

sealed interface Resource<D, E : RootError> {
    class Success<D, E : RootError>(val data: D) : Resource<D, E>
    data class Error<D, E : RootError>(val error: E) : Resource<D, E>
    class Loading<D, E : RootError> : Resource<D, E>
}
