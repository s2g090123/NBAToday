package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.STATS_BASE_URL
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.StatsNbaService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NbaRemoteDataSource : RemoteDataSource() {

    companion object {
        private const val HEADER_STATS_REFER = "https://www.nba.com/"
    }

    private val cdnGson = GsonBuilder().create()
    private val statsGson = GsonBuilder().create()

    private val cdnRetrofit = Retrofit.Builder()
        .baseUrl(CDN_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(cdnGson))
        .build()
    private val statsRetrofit = Retrofit.Builder()
        .baseUrl(STATS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(statsGson))
        .client(buildStatsOkHttpClient())
        .build()

    val cdnService by lazy {
        cdnRetrofit.create(CdnNbaService::class.java)
    }
    val statsService by lazy {
        statsRetrofit.create(StatsNbaService::class.java)
    }

    override suspend fun getSchedule(): Schedule? {
        return cdnService.getScheduleLeague()
    }

    private fun buildStatsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Refer", HEADER_STATS_REFER)
                        .build()
                    chain.proceed(request)
                }
            )
            .build()
    }
}