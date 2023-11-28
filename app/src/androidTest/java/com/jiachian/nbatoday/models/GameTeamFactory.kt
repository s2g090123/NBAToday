package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.models.local.game.GameTeam

object GameTeamFactory {

    fun getDefaultHomeTeam(): GameTeam {
        return GameTeam(
            team = HomeTeam,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
            periods = getWinPeriod()
        )
    }

    fun getDefaultAwayTeam(): GameTeam {
        return GameTeam(
            team = AwayTeam,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
            periods = getLosePeriod()
        )
    }

    private fun getWinPeriod(): List<GameTeam.Period> {
        return (1..4).map {
            GameTeam.Period(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }

    private fun getLosePeriod(): List<GameTeam.Period> {
        return (1..4).map {
            GameTeam.Period(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }
}
