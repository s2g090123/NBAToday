package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.Schedule
import retrofit2.http.GET

interface CdnNbaService {
    @GET("static/json/staticData/scheduleLeagueV2_32.json")
    suspend fun getScheduleLeague(): Schedule?
}