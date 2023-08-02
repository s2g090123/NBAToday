package com.jiachian.nbatoday.network

import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import java.io.IOException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExcludeFromJacocoGeneratedReport
class NbaResponseCall<S : Any>(
    private val delegate: Call<S>,
) : Call<NbaResponse<S>> {

    override fun enqueue(callback: Callback<NbaResponse<S>>) {
        return delegate.enqueue(
            @ExcludeFromJacocoGeneratedReport
            object : Callback<S> {
                override fun onResponse(call: Call<S>, response: Response<S>) {
                    val body = response.body()
                    val code = response.code()
                    val message = response.message()

                    if (response.isSuccessful) {
                        if (body != null) {
                            callback.onResponse(
                                this@NbaResponseCall,
                                Response.success(NbaResponse.Success(body))
                            )
                        } else {
                            callback.onResponse(
                                this@NbaResponseCall,
                                Response.success(NbaResponse.UnknownError(null))
                            )
                        }
                    } else {
                        callback.onResponse(
                            this@NbaResponseCall,
                            Response.success(NbaResponse.ApiError(message, code))
                        )
                    }
                }

                override fun onFailure(call: Call<S>, throwable: Throwable) {
                    val networkResponse = when (throwable) {
                        is IOException -> NbaResponse.NetworkError(throwable)
                        else -> NbaResponse.UnknownError(throwable)
                    }
                    callback.onResponse(this@NbaResponseCall, Response.success(networkResponse))
                }
            }
        )
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NbaResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NbaResponse<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}