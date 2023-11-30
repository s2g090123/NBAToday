package com.jiachian.nbatoday.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

abstract class BaseRepository {
    protected val isProgressingImp = MutableStateFlow(false)
    val isProgressing = isProgressingImp.asStateFlow()

    protected fun Response<*>.isError() = !isSuccessful || body() == null

    protected suspend fun <T> loading(runnable: suspend () -> T): T {
        return try {
            isProgressingImp.value = true
            runnable()
        } finally {
            isProgressingImp.value = false
        }
    }
}
