package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.remote.RemoteTeamGenerator
import com.jiachian.nbatoday.models.remote.team.extensions.toTeamStats
import com.jiachian.nbatoday.team.data.model.local.Team

object TeamGenerator {
    fun getHome(): Team {
        return RemoteTeamGenerator
            .get()
            .toTeamStats()
            .first { it.teamId == HomeTeamId }
    }

    fun getAway(): Team {
        return RemoteTeamGenerator
            .get()
            .toTeamStats()
            .first { it.teamId == AwayTeamId }
    }
}
