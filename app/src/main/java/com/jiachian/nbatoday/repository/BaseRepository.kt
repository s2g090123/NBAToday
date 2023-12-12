package com.jiachian.nbatoday.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

abstract class BaseRepository {
    private val isLoadingImp = MutableStateFlow(false)
    val isLoading = isLoadingImp.asStateFlow()

    @Volatile
    private var enqueueJobAmount = 0

    protected fun Response<*>.isError() = !isSuccessful || body() == null

    protected suspend fun <T> loading(runnable: suspend () -> T): T {
        return try {
            isLoadingImp.value = true
            enqueueJobAmount++
            runnable()
        } finally {
            enqueueJobAmount--
            if (enqueueJobAmount == 0) {
                isLoadingImp.value = false
            }
        }
    }
}
