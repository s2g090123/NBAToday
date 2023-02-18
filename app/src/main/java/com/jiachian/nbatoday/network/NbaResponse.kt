package com.jiachian.nbatoday.network

import java.io.IOException

sealed class NbaResponse<out T : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NbaResponse<T>()

    /**
     * Failure response with errorMessage
     */
    data class ApiError(val errorMessage: String, val code: Int) : NbaResponse<Nothing>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NbaResponse<Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NbaResponse<Nothing>()
}