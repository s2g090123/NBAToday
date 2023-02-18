package com.jiachian.nbatoday.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NbaResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) {
            return null
        }
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NbaResponse<T>>"
        }
        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NbaResponse::class.java) {
            return null
        }
        check(responseType is ParameterizedType) {
            "Response must be parameterized as NbaResponse<T>"
        }

        val resultType = getParameterUpperBound(0, responseType)
        return NbaResponseAdapter<Any>(resultType)
    }
}