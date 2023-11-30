package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.TeamService
import retrofit2.Response

class NBATeamRemoteSource : TeamRemoteSource() {

    private val teamService by lazy {
        retrofit.create(TeamService::class.java)
    }

    override suspend fun getTeamStats(teamId: Int?): Response<RemoteTeamStats> {
        return teamService.getTeamStats(season = CurrentSeason, teamId = teamId)
    }

    override suspend fun getTeamPlayerStats(teamId: Int): Response<RemoteTeamPlayerStats> {
        return teamService.getTeamPlayerStats(season = CurrentSeason, teamId = teamId)
    }
}
