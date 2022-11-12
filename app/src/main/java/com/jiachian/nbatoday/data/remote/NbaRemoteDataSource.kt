package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.StatsNbaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NbaRemoteDataSource : RemoteDataSource() {

    companion object {
        private const val CDN_BASE_URL = "https://cdn.nba.com/"
        private const val STATS_BASE_URL = "https://stats.nba.com/"
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
}