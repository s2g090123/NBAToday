package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.game.data.model.local.GameStatus
import com.jiachian.nbatoday.game.data.model.remote.GameDto

object RemoteGameGenerator {
    fun get(): GameDto {
        return GameDto(
            scoreboard = getScoreboard()
        )
    }

    private fun getScoreboard(): GameDto.RemoteScoreboard {
        return GameDto.RemoteScoreboard(
            games = listOf(getFinalGameDetail(), getPlayingGameDetail(), getComingSoonGameDetail())
        )
    }

    private fun getFinalGameDetail(): GameDto.RemoteScoreboard.RemoteGameDetail {
        return GameDto.RemoteScoreboard.RemoteGameDetail(
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL.code,
            gameStatusText = GameStatusFinal,
            gameLeaders = getGameLeaders(),
            teamLeaders = getGameLeaders(),
            homeTeam = getHomeGameTeam(),
            awayTeam = getAwayGameTeam()
        )
    }

    private fun getPlayingGameDetail(): GameDto.RemoteScoreboard.RemoteGameDetail {
        return GameDto.RemoteScoreboard.RemoteGameDetail(
            gameId = PlayingGameId,
            gameStatus = GameStatus.PLAYING.code,
            gameStatusText = GameStatusPrepare,
            gameLeaders = getGameLeaders(),
            teamLeaders = getGameLeaders(),
            homeTeam = getHomeGameTeam(),
            awayTeam = getAwayGameTeam()
        )
    }

    private fun getComingSoonGameDetail(): GameDto.RemoteScoreboard.RemoteGameDetail {
        return GameDto.RemoteScoreboard.RemoteGameDetail(
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON.code,
            gameStatusText = GameStatusPrepare,
            gameLeaders = getGameLeaders(),
            teamLeaders = getGameLeaders(),
            homeTeam = getHomeGameTeam(),
            awayTeam = getAwayGameTeam()
        )
    }

    private fun getGameLeaders(): GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders {
        return GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders(
            homeLeader = getHomeGameLeader(),
            awayLeader = getAwayGameLeader(),
        )
    }

    private fun getHomeGameLeader(): GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader {
        return GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader(
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

    private fun getAwayGameLeader(): GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader {
        return GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader(
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

    private fun getHomeGameTeam(): GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
            teamId = HomeTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }

    private fun getAwayGameTeam(): GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam {
        return GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam(
            teamId = AwayTeamId,
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }
}
