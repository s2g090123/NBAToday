package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.models.local.game.GameLeaders

object GameLeadersGenerator {
    fun get(): GameLeaders {
        return GameLeaders(
            homeLeader = getHome(),
            awayLeader = getAway(),
        )
    }

    private fun getHome(): GameLeaders.GameLeader {
        return GameLeaders.GameLeader(
            playerId = HomePlayerId,
            name = HomePlayerFullName,
            jerseyNum = BasicNumber.toString(),
            position = BasicPosition,
            teamTricode = HomeTeamAbbr,
            points = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble()
        )
    }

    private fun getAway(): GameLeaders.GameLeader {
        return GameLeaders.GameLeader(
            playerId = AwayPlayerId,
            name = AwayPlayerFullName,
            jerseyNum = BasicNumber.toString(),
            position = BasicPosition,
            teamTricode = AwayTeamAbbr,
            points = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble()
        )
    }
}
