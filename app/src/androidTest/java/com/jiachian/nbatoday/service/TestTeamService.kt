package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.RemoteTeamGenerator
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.team.data.TeamService
import com.jiachian.nbatoday.team.data.model.remote.TeamDto
import com.jiachian.nbatoday.team.data.model.remote.TeamPlayerDto
import retrofit2.Response

class TestTeamService : TeamService {
    override suspend fun getTeamPlayer(teamId: Int, season: String): Response<TeamPlayerDto> {
        return Response.success(RemoteTeamPlayerGenerator.get(teamId))
    }

    override suspend fun getTeam(season: String, teamId: Int?): Response<TeamDto> {
        return teamId?.let {
            Response.success(RemoteTeamGenerator.get(it))
        } ?: Response.success(RemoteTeamGenerator.get())
    }
}
