package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats

abstract class TeamRemoteSource : RemoteSource() {
    abstract suspend fun getTeamStats(teamId: Int?): RemoteTeamStats?
    abstract suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats?
}
