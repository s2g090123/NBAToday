package com.jiachian.nbatoday.network

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NbaResponseAdapter<S : Any>(
    private val resultType: Type
) : CallAdapter<S, Call<NbaResponse<S>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<S>): Call<NbaResponse<S>> {
        return NbaResponseCall(call)
    }
}