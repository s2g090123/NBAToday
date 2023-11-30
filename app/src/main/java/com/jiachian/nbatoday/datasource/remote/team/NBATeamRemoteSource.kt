package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.TeamService

class NBATeamRemoteSource : TeamRemoteSource() {

    private val teamService by lazy {
        retrofit.create(TeamService::class.java)
    }

    override suspend fun getTeamStats(teamId: Int?): RemoteTeamStats? {
        return teamService.getTeamStats(season = CurrentSeason, teamId = teamId).body()
    }

    override suspend fun getTeamPlayerStats(teamId: Int): RemoteTeamPlayerStats? {
        return teamService.getTeamPlayersStats(season = CurrentSeason, teamId = teamId).body()
    }
}
