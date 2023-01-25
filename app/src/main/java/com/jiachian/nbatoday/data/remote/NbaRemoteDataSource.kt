package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.NBA_SERVER_URL
import com.jiachian.nbatoday.STATS_BASE_URL
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.NbaService
import com.jiachian.nbatoday.service.StatsNbaService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NbaRemoteDataSource(private val dataStore: NbaDataStore) : RemoteDataSource() {

    private val gson = GsonBuilder().create()

    private val cdnRetrofit = Retrofit.Builder()
        .baseUrl(CDN_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    private val statsRetrofit = Retrofit.Builder()
        .baseUrl(STATS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(buildStatsOkHttpClient())
        .build()
    private val nbaRetrofit = Retrofit.Builder()
        .baseUrl(NBA_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(buildNBAOkHttpClient())
        .build()

    val cdnService by lazy {
        cdnRetrofit.create(CdnNbaService::class.java)
    }
    val statsService by lazy {
        statsRetrofit.create(StatsNbaService::class.java)
    }
    val nbaService by lazy {
        nbaRetrofit.create(NbaService::class.java)
    }

    override suspend fun getSchedule(): Schedule? {
        return nbaService.getSchedule()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard? {
        return nbaService.getScoreboard(leagueId, gameDate)
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore? {
        return nbaService.getGameBoxScore(gameId)
    }

    override suspend fun getTeamStats(): RemoteTeamStats? {
        return nbaService.getTeamStats(season = "2022-23")
    }

    override suspend fun getTeamStats(teamId: Int): RemoteTeamStats? {
        return nbaService.getTeamStats(season = "2022-23", teamId = teamId)
    }

    override suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats? {
        return nbaService.getTeamPlayersStats(season = "2022-23", teamId = teamId)
    }

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo? {
        return nbaService.getPlayerInfo(playerId)
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats? {
        return nbaService.getPlayerStats(season = "2022-23", playerId = playerId)
    }

    private fun buildStatsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                runBlocking {
                    val response = chain.proceed(chain.request())
                    if (response.headers("Set-Cookie").isNotEmpty()) {
                        val cookies = mutableSetOf<String>()
                        response.headers("Set-Cookie").forEach {
                            cookies.add(it)
                        }
                        dataStore.updateStatsCookies(cookies)
                    }
                    response
                }
            }
            .addInterceptor(
                Interceptor { chain ->
                    runBlocking {
                        val cookies = dataStore.statsCookies.first()
                        val request = chain.request()
                            .newBuilder()
                            .addHeader("Referer", "https://stats.nba.com/")
                            .addHeader("Connection", "keep-alive")
                            .addHeader(
                                "User-Agent",
                                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
                            )
                            .addHeader("Accept", "*/*")
                            .apply {
                                cookies.forEach {
                                    addHeader("Cookie", it)
                                }
                            }
                            .build()
                        val response = chain.proceed(request)
                        response
                    }
                }
            )
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun buildNBAOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}