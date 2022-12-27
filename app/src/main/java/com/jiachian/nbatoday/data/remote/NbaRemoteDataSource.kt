package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.STATS_BASE_URL
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.StatsNbaService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NbaRemoteDataSource : RemoteDataSource() {

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

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard? {
        return statsService.getScoreboard(leagueId, gameDate)
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore? {
        return cdnService.getGameBoxScore(gameId)
    }

    override suspend fun getTeamStats(): RemoteTeamStats? {
        return statsService.getTeamStats("2022-23")
    }

    override suspend fun getTeamStats(teamId: Int): RemoteTeamStats? {
        return statsService.getTeamStats(season = "2022-23", teamId = teamId)
    }

    private fun buildStatsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    runBlocking {
                        val request = chain.request()
                            .newBuilder()
                            .addHeader("Referer", "https://stats.nba.com/")
                            .addHeader("Connection", "keep-alive")
                            .addHeader(
                                "User-Agent",
                                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
                            )
                            .addHeader("Accept", "*/*")
                            .build()
                        chain.proceed(request)
                    }
                }
            )
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}