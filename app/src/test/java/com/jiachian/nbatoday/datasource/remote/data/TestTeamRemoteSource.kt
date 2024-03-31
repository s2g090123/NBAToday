package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemoteTeamGenerator
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.team.data.model.remote.TeamDto
import com.jiachian.nbatoday.team.data.model.remote.TeamPlayerDto
import retrofit2.Response

class TestTeamRemoteSource : TeamRemoteSource() {
    override suspend fun getTeam(): Response<TeamDto> {
        return Response.success(RemoteTeamGenerator.get())
    }

    override suspend fun getTeam(teamId: Int): Response<TeamDto> {
        return Response.success(RemoteTeamGenerator.get(teamId))
    }

    override suspend fun getTeamPlayer(teamId: Int): Response<TeamPlayerDto> {
        return Response.success(RemoteTeamPlayerGenerator.get(teamId))
    }
}
