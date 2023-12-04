package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import com.jiachian.nbatoday.service.TeamService
import retrofit2.Response

class NBATeamRemoteSource : TeamRemoteSource() {

    private val teamService by lazy {
        retrofit.create(TeamService::class.java)
    }

    override suspend fun getTeamStats(): Response<RemoteTeam> {
        return teamService.getTeamStats(season = CurrentSeason, teamId = null)
    }

    override suspend fun getTeamStats(teamId: Int): Response<RemoteTeam> {
        return teamService.getTeamStats(season = CurrentSeason, teamId = teamId)
    }

    override suspend fun getTeamPlayerStats(teamId: Int): Response<RemoteTeamPlayer> {
        return teamService.getTeamPlayerStats(season = CurrentSeason, teamId = teamId)
    }
}
