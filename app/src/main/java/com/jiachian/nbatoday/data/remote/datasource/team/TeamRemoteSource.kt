package com.jiachian.nbatoday.data.remote.datasource.team

import com.jiachian.nbatoday.data.remote.datasource.RemoteSource
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats

abstract class TeamRemoteSource : RemoteSource() {
    abstract suspend fun getTeamStats(teamId: Int?): RemoteTeamStats?
    abstract suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats?
}
