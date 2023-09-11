package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.AWAY_PLAYER_FULL_NAME
import com.jiachian.nbatoday.AWAY_PLAYER_ID
import com.jiachian.nbatoday.AWAY_PLAYER_LAST_NAME
import com.jiachian.nbatoday.AWAY_TEAM_ABBR
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.AWAY_TEAM_NAME
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.BASIC_POSITION
import com.jiachian.nbatoday.HOME_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.HOME_PLAYER_FULL_NAME
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_PLAYER_LAST_NAME
import com.jiachian.nbatoday.HOME_TEAM_ABBR
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.HOME_TEAM_NAME
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.leader.GameLeaders

object GameLeaderFactory {

    fun getHomePointsLeader(): NbaGame.NbaPointsLeader {
        return NbaGame.NbaPointsLeader(
            firstName = HOME_PLAYER_FIRST_NAME,
            lastName = HOME_PLAYER_LAST_NAME,
            personId = HOME_PLAYER_ID,
            points = BASIC_NUMBER.toDouble(),
            teamId = HOME_TEAM_ID,
            teamName = HOME_TEAM_NAME,
            teamTricode = HOME_TEAM_ABBR
        )
    }

    fun getAwayPointsLeader(): NbaGame.NbaPointsLeader {
        return NbaGame.NbaPointsLeader(
            firstName = AWAY_PLAYER_FIRST_NAME,
            lastName = AWAY_PLAYER_LAST_NAME,
            personId = AWAY_PLAYER_ID,
            points = BASIC_NUMBER.toDouble(),
            teamId = AWAY_TEAM_ID,
            teamName = AWAY_TEAM_NAME,
            teamTricode = AWAY_TEAM_ABBR
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
            personId = HOME_PLAYER_ID,
            name = HOME_PLAYER_FULL_NAME,
            jerseyNum = BASIC_NUMBER.toString(),
            position = BASIC_POSITION,
            teamTricode = HOME_TEAM_ABBR,
            points = BASIC_NUMBER.toDouble(),
            rebounds = BASIC_NUMBER.toDouble(),
            assists = BASIC_NUMBER.toDouble()
        )
    }

    private fun getAwayLeader(): GameLeaders.GameLeader {
        return GameLeaders.GameLeader(
            personId = AWAY_PLAYER_ID,
            name = AWAY_PLAYER_FULL_NAME,
            jerseyNum = BASIC_NUMBER.toString(),
            position = BASIC_POSITION,
            teamTricode = AWAY_TEAM_ABBR,
            points = BASIC_NUMBER.toDouble(),
            rebounds = BASIC_NUMBER.toDouble(),
            assists = BASIC_NUMBER.toDouble()
        )
    }
}
