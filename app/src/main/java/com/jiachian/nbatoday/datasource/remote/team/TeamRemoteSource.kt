package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import retrofit2.Response

abstract class TeamRemoteSource : RemoteSource() {
    abstract suspend fun getTeamStats(): Response<RemoteTeam>
    abstract suspend fun getTeamStats(teamId: Int): Response<RemoteTeam>
    abstract suspend fun getTeamPlayerStats(teamId: Int): Response<RemoteTeamPlayer>
}
