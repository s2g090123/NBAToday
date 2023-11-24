package com.jiachian.nbatoday.data

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
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.leader.GameLeaders

object GameLeaderFactory {

    fun getHomePointsLeader(): NbaGame.NbaPointsLeader {
        return NbaGame.NbaPointsLeader(
            firstName = HomePlayerFirstName,
            lastName = HomePlayerLastName,
            personId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId,
            teamName = HomeTeamName,
            teamTricode = HomeTeamAbbr
        )
    }

    fun getAwayPointsLeader(): NbaGame.NbaPointsLeader {
        return NbaGame.NbaPointsLeader(
            firstName = AwayPlayerFirstName,
            lastName = AwayPlayerLastName,
            personId = AwayPlayerId,
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
            personId = HomePlayerId,
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
            personId = AwayPlayerId,
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
