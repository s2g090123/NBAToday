package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemoteTeamGenerator
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import retrofit2.Response

class TestTeamRemoteSource : TeamRemoteSource() {
    override suspend fun getTeam(): Response<RemoteTeam> {
        return Response.success(RemoteTeamGenerator.get())
    }

    override suspend fun getTeam(teamId: Int): Response<RemoteTeam> {
        return Response.success(RemoteTeamGenerator.get(teamId))
    }

    override suspend fun getTeamPlayer(teamId: Int): Response<RemoteTeamPlayer> {
        return Response.success(RemoteTeamPlayerGenerator.get(teamId))
    }
}
