package com.jiachian.nbatoday.utils

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.common.data.NBAServerUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Creating and managing Retrofit instances for remote data sources.
 */
object RetrofitUtils {
    private const val ConnectTimeOut = 5L
    private const val ReadTimeOut = 20L

    private val gson by lazy {
        GsonBuilder().setLenient().create()
    }

    @Volatile
    private var retrofit: Retrofit? = null

    fun <T> createService(c: Class<T>): T {
        return getRetrofit().create(c)
    }

    private fun getRetrofit(): Retrofit {
        return synchronized(Unit) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(NBAServerUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(buildHttpClient())
                .build()
                .also { retrofit = it }
        }
    }

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ConnectTimeOut, TimeUnit.SECONDS)
            .readTimeout(ReadTimeOut, TimeUnit.SECONDS)
            .build()
    }
}
