package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import com.jiachian.nbatoday.service.TeamService
import retrofit2.Response

class NBATeamRemoteSource(
    private val teamService: TeamService
) : TeamRemoteSource() {
    override suspend fun getTeam(): Response<RemoteTeam> {
        return teamService.getTeam(season = CurrentSeason, teamId = null)
    }

    override suspend fun getTeam(teamId: Int): Response<RemoteTeam> {
        return teamService.getTeam(season = CurrentSeason, teamId = teamId)
    }

    override suspend fun getTeamPlayer(teamId: Int): Response<RemoteTeamPlayer> {
        return teamService.getTeamPlayer(season = CurrentSeason, teamId = teamId)
    }
}
