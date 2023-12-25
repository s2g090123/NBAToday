package com.jiachian.nbatoday.datasource.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.NBAServerUrl
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class RemoteSource {
    companion object {
        private const val ConnectTimeOut = 5L
        private const val ReadTimeOut = 20L

        private val gson = GsonBuilder().setLenient().create()
        private var retrofit: Retrofit? = null

        fun <T> createService(c: Class<T>): T {
            return getRetrofit().create(c)
        }

        private fun getRetrofit(): Retrofit {
            return retrofit ?: synchronized(Unit) {
                Retrofit.Builder()
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
}
