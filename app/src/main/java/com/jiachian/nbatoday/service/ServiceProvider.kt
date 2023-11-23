package com.jiachian.nbatoday.service

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.NBA_SERVER_URL
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceProvider {
    private val gson = GsonBuilder().setLenient().create()

    private val nbaRetrofit = Retrofit.Builder()
        .baseUrl(NBA_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(buildNBAOkHttpClient())
        .build()

    val nbaService by lazy {
        nbaRetrofit.create(NbaService::class.java)
    }

    private fun buildNBAOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}
