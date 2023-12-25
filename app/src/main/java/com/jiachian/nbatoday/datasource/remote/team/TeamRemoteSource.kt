package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import retrofit2.Response

abstract class TeamRemoteSource {
    abstract suspend fun getTeam(): Response<RemoteTeam>
    abstract suspend fun getTeam(teamId: Int): Response<RemoteTeam>
    abstract suspend fun getTeamPlayer(teamId: Int): Response<RemoteTeamPlayer>
}
