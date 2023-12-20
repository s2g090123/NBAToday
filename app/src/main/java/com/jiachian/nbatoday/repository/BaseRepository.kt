package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.send
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

abstract class BaseRepository {
    private val loadingImp = MutableStateFlow(false)
    val loading = loadingImp.asStateFlow()

    @Volatile
    private var enqueueJobAmount = 0

    protected fun Response<*>.isError() = !isSuccessful || body() == null

    protected suspend fun <T> loading(runnable: suspend () -> T): T {
        return try {
            loadingImp.value = true
            enqueueJobAmount++
            runnable()
        } finally {
            enqueueJobAmount--
            if (enqueueJobAmount == 0) {
                loadingImp.value = false
            }
        }
    }

    protected fun onError() {
        ToastEvent.onError.send()
    }
}
