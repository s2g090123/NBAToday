package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.remote.game.RemoteGame

object RemoteGameGenerator {
    fun getFinal(): RemoteGame {
        return RemoteGame(
            scoreboard = getFinalScoreboard()
        )
    }

    private fun getFinalScoreboard(): RemoteGame.RemoteScoreboard {
        return RemoteGame.RemoteScoreboard(
            games = listOf(getFinalGameDetail())
        )
    }

    private fun getFinalGameDetail(): RemoteGame.RemoteScoreboard.RemoteGameDetail {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail(
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL.code,
            gameStatusText = GameStatusFinal,
            gameLeaders = getGameLeaders(),
            teamLeaders = getGameLeaders(),
            homeTeam = getHomeGameTeam(),
            awayTeam = getAwayGameTeam()
        )
    }

    private fun getGameLeaders(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders(
            homeLeader = getHomeGameLeader(),
            awayLeader = getAwayGameLeader(),
        )
    }

    private fun getHomeGameLeader(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader(
            playerId = HomePlayerId,
            name = HomePlayerFullName,
            jerseyNum = BasicNumber.toString(),
            position = BasicPosition,
            teamTricode = HomeTeamAbbr,
            points = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble(),
        )
    }

    private fun getAwayGameLeader(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader(
            playerId = AwayPlayerId,
            name = AwayPlayerFullName,
            jerseyNum = BasicNumber.toString(),
            position = BasicPosition,
            teamTricode = AwayTeamAbbr,
            points = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble(),
        )
    }

    private fun getHomeGameTeam(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
            teamId = HomeTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }

    private fun getAwayGameTeam(): RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
            teamId = AwayTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }
}
