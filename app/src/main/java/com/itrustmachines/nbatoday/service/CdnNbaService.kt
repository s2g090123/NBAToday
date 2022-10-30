package com.itrustmachines.nbatoday.service

import com.itrustmachines.nbatoday.data.schedule.Schedule
import retrofit2.http.GET

interface CdnNbaService {
    @GET("static/json/staticData/scheduleLeagueV2_32.json")
    fun getScheduleLeague(): Schedule?
}