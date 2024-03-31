package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameDateTime
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameDateTime
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameDateTime
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.game.data.model.local.GameStatus
import com.jiachian.nbatoday.game.data.model.remote.ScheduleDto

object RemoteScheduleGenerator {
    fun get(): ScheduleDto {
        return ScheduleDto(
            leagueSchedule = getLeagueSchedule()
        )
    }

    private fun getLeagueSchedule(): ScheduleDto.RemoteLeagueSchedule {
        return ScheduleDto.RemoteLeagueSchedule(
            gameDates = listOf(
                getGameDate()
            )
        )
    }

    private fun getGameDate(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate(
            games = listOf(
                getFinalGame(),
                getPlayingGame(),
                getComingSoonGame()
            )
        )
    }

    private fun getFinalGame(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = FinalGameDateTime,
            gameDateTimeEst = FinalGameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getPlayingGame(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = PlayingGameId,
            gameStatus = GameStatus.PLAYING,
            gameStatusText = GameStatusPrepare,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = PlayingGameDateTime,
            gameDateTimeEst = PlayingGameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getComingSoonGame(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
            awayTeam = getAwayTeam(),
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON,
            gameStatusText = GameStatusPrepare,
            gameSequence = 0,
            homeTeam = getHomeTeam(),
            gameDateEst = ComingSoonGameDateTime,
            gameDateTimeEst = ComingSoonGameDateTime,
            pointsLeaders = listOf(
                getHomeLeader(),
                getAwayLeader()
            )
        )
    }

    private fun getHomeTeam(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam(
            losses = BasicNumber,
            score = BasicNumber,
            teamId = HomeTeamId,
            wins = BasicNumber
        )
    }

    private fun getAwayTeam(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam(
            losses = BasicNumber,
            score = BasicNumber,
            teamId = AwayTeamId,
            wins = BasicNumber
        )
    }

    private fun getHomeLeader(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader(
            playerId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId
        )
    }

    private fun getAwayLeader(): ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader {
        return ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader(
            playerId = AwayPlayerId,
            points = BasicNumber.toDouble(),
            teamId = AwayTeamId
        )
    }
}
