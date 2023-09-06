package com.jiachian.nbatoday.network

import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import java.io.IOException

sealed class NbaResponse<out T : Any> {
    /**
     * Success response with body
     */
    @ExcludeFromJacocoGeneratedReport
    data class Success<T : Any>(val body: T) : NbaResponse<T>()

    /**
     * Failure response with errorMessage
     */
    @ExcludeFromJacocoGeneratedReport
    data class ApiError(val errorMessage: String, val code: Int) : NbaResponse<Nothing>()

    /**
     * Network error
     */
    @ExcludeFromJacocoGeneratedReport
    data class NetworkError(val error: IOException) : NbaResponse<Nothing>()

    /**
     * For example, json parsing error
     */
    @ExcludeFromJacocoGeneratedReport
    data class UnknownError(val error: Throwable?) : NbaResponse<Nothing>()
}