package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import retrofit2.http.GET
import retrofit2.http.Path

@Deprecated("Replace with NbaService", ReplaceWith("NbaService"))
interface CdnNbaService {
    @GET("static/json/staticData/scheduleLeagueV2_32.json")
    suspend fun getScheduleLeague(): Schedule?

    @GET("static/json/liveData/boxscore/boxscore_{gameId}.json")
    suspend fun getGameBoxScore(@Path("gameId") gameId: String): RemoteGameBoxScore?
}
