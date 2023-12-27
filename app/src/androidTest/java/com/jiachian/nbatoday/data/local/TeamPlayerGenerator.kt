package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.remote.RemoteTeamPlayerGenerator
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.remote.team.extensions.toTeamPlayerStats

object TeamPlayerGenerator {
    fun getHome(): TeamPlayer {
        return RemoteTeamPlayerGenerator
            .get(HomeTeamId)
            .toTeamPlayerStats()
            .first()
    }

    fun getAway(): TeamPlayer {
        return RemoteTeamPlayerGenerator
            .get(HomeTeamId)
            .toTeamPlayerStats()
            .first()
    }
}
