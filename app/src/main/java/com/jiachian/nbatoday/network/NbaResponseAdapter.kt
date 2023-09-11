package com.jiachian.nbatoday.network

import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

@ExcludeFromJacocoGeneratedReport
class NbaResponseAdapter<S : Any>(
    private val resultType: Type
) : CallAdapter<S, Call<NbaResponse<S>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<S>): Call<NbaResponse<S>> {
        return NbaResponseCall(call)
    }
}
