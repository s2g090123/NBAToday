package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository {
    private val isProgressingImp = MutableStateFlow(false)
    val isProgressing = isProgressingImp.asStateFlow()

    protected fun Response<*>.isError() = !isSuccessful || body() == null

    protected suspend fun <T> loading(runnable: suspend () -> T): T {
        val isLoadingEarly = isProgressing.value
        return try {
            isProgressingImp.value = true
            runnable()
        } finally {
            if (!isLoadingEarly) {
                isProgressingImp.value = false
            }
        }
    }

    protected suspend fun showError() {
        withContext(Dispatchers.Main) {
            showErrorToast()
        }
    }
}
