package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.CURRENT_SEASON
import com.jiachian.nbatoday.NBA_SERVER_URL
import com.jiachian.nbatoday.STATS_BASE_URL
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.data.remote.user.LoginBody
import com.jiachian.nbatoday.data.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.data.remote.user.UpdatePointBody
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.NbaService
import com.jiachian.nbatoday.service.StatsNbaService
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NbaRemoteDataSource(private val dataStore: BaseDataStore) : RemoteDataSource() {

    private val gson = GsonBuilder().setLenient().create()

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
        return nbaService.getSchedule().body()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard? {
        return nbaService.getScoreboard(leagueId, gameDate).body()
    }

    override suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<GameScoreboard>? {
        return nbaService.getScoreboards(leagueId, year, month, day, offset).body()
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore? {
        return nbaService.getGameBoxScore(gameId).body()
    }

    override suspend fun getTeamStats(): RemoteTeamStats? {
        return nbaService.getTeamStats(season = CURRENT_SEASON).body()
    }

    override suspend fun getTeamStats(teamId: Int): RemoteTeamStats? {
        return nbaService.getTeamStats(season = CURRENT_SEASON, teamId = teamId).body()
    }

    override suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats? {
        return nbaService.getTeamPlayersStats(season = CURRENT_SEASON, teamId = teamId).body()
    }

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo? {
        return nbaService.getPlayerInfo(playerId).body()
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats? {
        return nbaService.getPlayerStats(season = CURRENT_SEASON, playerId = playerId).body()
    }

    override suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail? {
        return nbaService.getPlayerDetail(season = CURRENT_SEASON, playerId = playerId).body()
    }

    override suspend fun login(account: String, password: String): User? {
        return nbaService.login(LoginBody(account, password)).body()
    }

    override suspend fun register(account: String, password: String): User? {
        return nbaService.register(LoginBody(account, password)).body()
    }

    override suspend fun updatePassword(account: String, password: String, token: String): String? {
        return nbaService.updatePassword(UpdatePasswordBody(account, token, password)).body()
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): String? {
        return nbaService.updatePoints(UpdatePointBody(account, token, points)).body()
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