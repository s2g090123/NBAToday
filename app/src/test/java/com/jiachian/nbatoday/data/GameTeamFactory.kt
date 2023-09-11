package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_TEAM_ABBR
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.AWAY_TEAM_NAME
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.HOME_TEAM_ABBR
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.HOME_TEAM_NAME
import com.jiachian.nbatoday.TEAM_CITY
import com.jiachian.nbatoday.data.remote.team.GameTeam

object GameTeamFactory {

    fun getDefaultHomeTeam(): GameTeam {
        return GameTeam(
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
            teamCity = TEAM_CITY,
            teamId = HOME_TEAM_ID,
            teamName = HOME_TEAM_NAME,
            teamTricode = HOME_TEAM_ABBR,
            wins = BASIC_NUMBER,
            periods = getWinPeriod()
        )
    }

    fun getDefaultAwayTeam(): GameTeam {
        return GameTeam(
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
            teamCity = TEAM_CITY,
            teamId = AWAY_TEAM_ID,
            teamName = AWAY_TEAM_NAME,
            teamTricode = AWAY_TEAM_ABBR,
            wins = BASIC_NUMBER,
            periods = getLosePeriod()
        )
    }

    private fun getWinPeriod(): List<GameTeam.Period> {
        return (1..4).map {
            GameTeam.Period(
                period = it,
                periodType = "REGULAR",
                score = BASIC_NUMBER
            )
        }
    }

    private fun getLosePeriod(): List<GameTeam.Period> {
        return (1..4).map {
            GameTeam.Period(
                period = it,
                periodType = "REGULAR",
                score = BASIC_NUMBER
            )
        }
    }
}
