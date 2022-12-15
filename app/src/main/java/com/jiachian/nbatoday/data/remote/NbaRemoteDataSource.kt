package com.jiachian.nbatoday.data.remote

import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.STATS_BASE_URL
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.service.CdnNbaService
import com.jiachian.nbatoday.service.StatsNbaService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NbaRemoteDataSource(private val dataStore: NbaDataStore) : RemoteDataSource() {

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

    private fun buildStatsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                runBlocking {
                    val request = Request.Builder()
                        .method("GET", null)
                        .url(STATS_BASE_URL)
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .build()
                    val response = chain.proceed(request)
                    if (response.headers("Set-Cookie").isNotEmpty()) {
                        val cookies = mutableSetOf<String>()
                        response.headers("Set-Cookie").forEach {
                            cookies.add(it)
                        }
                        dataStore.updateStatsCookies(cookies)
                    }
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("")
                        .body("{}".toResponseBody(null))
                        .build()
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
                        chain.proceed(request)
                    }
                }
            )
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}