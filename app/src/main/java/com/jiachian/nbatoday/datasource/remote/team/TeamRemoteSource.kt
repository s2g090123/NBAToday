package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats
import retrofit2.Response

abstract class TeamRemoteSource : RemoteSource() {
    abstract suspend fun getTeamStats(teamId: Int?): Response<RemoteTeamStats>
    abstract suspend fun getTeamPlayerStats(teamId: Int): Response<RemoteTeamPlayerStats>
}
