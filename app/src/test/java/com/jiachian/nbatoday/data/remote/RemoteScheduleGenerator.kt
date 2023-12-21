package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameDateTime
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule

object RemoteScheduleGenerator {
    fun get(): RemoteSchedule {
        return RemoteSchedule(
            leagueSchedule = getLeagueSchedule()
        )
    }

    private fun getLeagueSchedule(): RemoteSchedule.RemoteLeagueSchedule {
        return RemoteSchedule.RemoteLeagueSchedule(
            gameDates = listOf(
                getGameDate()
            )
        )
    }

    private fun getGameDate(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate(
            games = listOf(
                getFinalGame(),
                getPlayingGame(),
                getComingSoonGame()
            )
        )
    }

    private fun getFinalGame(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = GameDateTime,
            gameDateTimeEst = GameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getPlayingGame(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = PlayingGameId,
            gameStatus = GameStatus.PLAYING,
            gameStatusText = GameStatusPrepare,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = GameDateTime,
            gameDateTimeEst = GameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getComingSoonGame(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON,
            gameStatusText = GameStatusPrepare,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = GameDateTime,
            gameDateTimeEst = GameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getHomeTeam(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam(
            losses = BasicNumber,
            score = BasicNumber,
            teamId = HomeTeamId,
            wins = BasicNumber
        )
    }

    private fun getAwayTeam(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam(
            losses = BasicNumber,
            score = BasicNumber,
            teamId = AwayTeamId,
            wins = BasicNumber
        )
    }

    private fun getHomeLeader(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader(
            playerId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId
        )
    }

    private fun getAwayLeader(): RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader {
        return RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader(
            playerId = AwayPlayerId,
            points = BasicNumber.toDouble(),
            teamId = AwayTeamId
        )
    }
}
