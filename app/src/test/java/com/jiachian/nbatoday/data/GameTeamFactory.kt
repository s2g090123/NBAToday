package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.team.GameTeam

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

    fun getDefaultRemoteHomeTeam(): GameScoreboard.Scoreboard.Game.RemoteGameTeam {
        return GameScoreboard.Scoreboard.Game.RemoteGameTeam(
            teamId = HomeTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
            periods = getRemoteWinPeriod()
        )
    }

    fun getDefaultRemoteAwayTeam(): GameScoreboard.Scoreboard.Game.RemoteGameTeam {
        return GameScoreboard.Scoreboard.Game.RemoteGameTeam(
            teamId = AwayTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
            periods = getRemoteLosePeriod()
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

    private fun getRemoteWinPeriod(): List<GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }

    private fun getRemoteLosePeriod(): List<GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }
}
