package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.models.remote.game.RemoteGame

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

    fun getDefaultRemoteHomeTeam(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
            teamId = HomeTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
            periods = getRemoteWinPeriod()
        )
    }

    fun getDefaultRemoteAwayTeam(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
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

    private fun getRemoteWinPeriod(): List<RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }

    private fun getRemoteLosePeriod(): List<RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.RemotePeriod> {
        return (1..4).map {
            RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.RemotePeriod(
                period = it,
                periodType = "REGULAR",
                score = BasicNumber
            )
        }
    }
}
