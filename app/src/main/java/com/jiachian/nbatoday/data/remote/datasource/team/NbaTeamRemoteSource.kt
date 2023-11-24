package com.jiachian.nbatoday.data.remote.datasource.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.TeamService

class NbaTeamRemoteSource : TeamRemoteSource() {

    private val teamService by lazy {
        retrofit.create(TeamService::class.java)
    }

    override suspend fun getTeamStats(teamId: Int?): RemoteTeamStats? {
        return teamService.getTeamStats(season = CurrentSeason, teamId = teamId).body()
    }

    override suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats? {
        return teamService.getTeamPlayersStats(season = CurrentSeason, teamId = teamId).body()
    }
}
