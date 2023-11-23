package com.jiachian.nbatoday.data.remote.datasource.team

import com.jiachian.nbatoday.CURRENT_SEASON
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.service.ServiceProvider

class NbaTeamRemoteSource : TeamRemoteSource() {

    private val nbaService = ServiceProvider.nbaService

    override suspend fun getTeamStats(teamId: Int?): RemoteTeamStats? {
        return nbaService.getTeamStats(season = CURRENT_SEASON, teamId = teamId).body()
    }

    override suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats? {
        return nbaService.getTeamPlayersStats(season = CURRENT_SEASON, teamId = teamId).body()
    }
}
