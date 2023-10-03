package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_TEAM
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.HOME_TEAM
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
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

    fun getDefaultRemoteHomeTeam(): GameScoreboard.Scoreboard.Game.RemoteGameTeam {
        return GameScoreboard.Scoreboard.Game.RemoteGameTeam(
            teamId = HOME_TEAM_ID,
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
            wins = BASIC_NUMBER,
            periods = getRemoteWinPeriod()
        )
    }

    fun getDefaultRemoteAwayTeam(): GameScoreboard.Scoreboard.Game.RemoteGameTeam {
        return GameScoreboard.Scoreboard.Game.RemoteGameTeam(
            teamId = AWAY_TEAM_ID,
            losses = BASIC_NUMBER,
            score = BASIC_NUMBER,
            wins = BASIC_NUMBER,
            periods = getRemoteLosePeriod()
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

    private fun getRemoteWinPeriod(): List<GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BASIC_NUMBER
            )
        }
    }

    private fun getRemoteLosePeriod(): List<GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            GameScoreboard.Scoreboard.Game.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BASIC_NUMBER
            )
        }
    }
}
