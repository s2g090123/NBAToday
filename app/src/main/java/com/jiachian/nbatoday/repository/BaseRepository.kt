package com.jiachian.nbatoday.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

abstract class BaseRepository {
    private val isLoadingImp = MutableStateFlow(false)
    val isLoading = isLoadingImp.asStateFlow()

    protected fun Response<*>.isError() = !isSuccessful || body() == null

    protected suspend fun <T> loading(runnable: suspend () -> T): T {
        val isLoadingEarly = isLoading.value
        return try {
            isLoadingImp.value = true
            runnable()
        } finally {
            if (!isLoadingEarly) {
                isLoadingImp.value = false
            }
        }
    }
}
