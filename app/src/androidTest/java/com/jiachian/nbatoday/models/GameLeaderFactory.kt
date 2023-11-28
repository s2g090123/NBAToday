package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayPlayerFirstName
import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameLeaders

object GameLeaderFactory {

    fun getHomePointsLeader(): Game.PointsLeader {
        return Game.PointsLeader(
            firstName = HomePlayerFirstName,
            lastName = HomePlayerLastName,
            playerId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId,
            teamName = HomeTeamName,
            teamTricode = HomeTeamAbbr
        )
    }

    fun getAwayPointsLeader(): Game.PointsLeader {
        return Game.PointsLeader(
            firstName = AwayPlayerFirstName,
            lastName = AwayPlayerLastName,
            playerId = AwayPlayerId,
            points = BasicNumber.toDouble(),
            teamId = AwayTeamId,
            teamName = AwayTeamName,
            teamTricode = AwayTeamAbbr
        )
    }

    fun getGameLeaders(): GameLeaders {
        return GameLeaders(
            homeLeaders = getHomeLeader(),
            awayLeaders = getAwayLeader()
        )
    }

    private fun getHomeLeader(): GameLeaders.GameLeader {
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

    private fun getAwayLeader(): GameLeaders.GameLeader {
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
