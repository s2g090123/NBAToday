package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_TEAM
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.HOME_TEAM
import com.jiachian.nbatoday.data.remote.team.GameTeam

object GameTeamFactory {

    fun getDefaultHomeTeam(): GameTeam {
        return GameTeam(
            team = HOME_TEAM,
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
            wins = BASIC_NUMBER,
            periods = getWinPeriod()
        )
    }

    fun getDefaultAwayTeam(): GameTeam {
        return GameTeam(
            team = AWAY_TEAM,
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
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
