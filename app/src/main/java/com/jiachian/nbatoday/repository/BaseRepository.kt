package com.jiachian.nbatoday.repository

import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.send
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

/**
 * Provide common functionality for other repositories.
 */
abstract class BaseRepository {
    /**
     * A Flow representing the loading state of the repository.
     */
    private val loadingImp = MutableStateFlow(false)
    val loading = loadingImp.asStateFlow()

    /**
     * A volatile variable to track the number of ongoing asynchronous tasks.
     */
    @Volatile
    private var enqueueJobAmount = 0

    /**
     * Checks if a Retrofit response indicates an error (either unsuccessful or with a null body).
     *
     * @return `true` if the response is an error, `false` otherwise.
     */
    protected fun Response<*>.isError() = !isSuccessful || body() == null

    /**
     * Wraps an asynchronous operation with loading state management.
     *
     * @param runnable The asynchronous operation to be executed.
     * @return The result of the asynchronous operation.
     */
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

    /**
     * Sends a toast event indicating an error.
     */
    protected fun onError() {
        ToastEvent.OnError.send()
    }
}
