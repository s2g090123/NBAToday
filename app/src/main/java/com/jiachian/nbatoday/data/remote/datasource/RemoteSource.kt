package com.jiachian.nbatoday.data.remote.datasource

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.NBA_SERVER_URL
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class RemoteSource {
    private val gson = GsonBuilder().setLenient().create()

    protected val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NBA_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(buildHttpClient())
        .build()

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}
