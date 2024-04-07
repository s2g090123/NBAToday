package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.team.data.model.toTeamPlayerStats

object TeamPlayerGenerator {
    fun getHome(): List<TeamPlayer> {
        return RemoteTeamPlayerGenerator
            .get(HomeTeamId)
            .toTeamPlayerStats()
    }

    fun getAway(): List<TeamPlayer> {
        return RemoteTeamPlayerGenerator
            .get(HomeTeamId)
            .toTeamPlayerStats()
    }
}
