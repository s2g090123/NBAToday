package com.jiachian.nbatoday.models.remote.game

import android.annotation.SuppressLint
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.SimpleDateFormat
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
fun RemoteSchedule.RemoteLeagueSchedule.toGames(): List<Game> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("EST")
    }
    return gameDates?.mapNotNull { gameDate ->
        gameDate.games?.mapNotNull { game ->
            createNbaGame(game, dateFormat)
        }
    }?.flatten() ?: emptyList()
}

private fun RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam.toGameTeam(): GameTeam {
    return GameTeam(
        team = teamId.let { NBATeam.getTeamById(it) },
        losses = losses.getOrZero(),
        score = score.getOrZero(),
        wins = wins.getOrZero(),
    )
}

private fun RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader.toPointsLeader():
    Game.PointsLeader? {
    if (playerId == null || teamId == null) return null
    val points = points.getOrZero()
    return Game.PointsLeader(
        playerId = playerId,
        points = points,
        teamId = teamId,
    )
}

private fun createNbaGame(
    game: RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame,
    dateFormat: SimpleDateFormat
): Game? {
    val gameDate = DateUtils.parseDate(game.gameDateEst, dateFormat)
    val gameDateTime = DateUtils.parseDate(game.gameDateTimeEst, dateFormat)
    val isDateValid = gameDate != null && gameDateTime != null

    val awayTeam = game.awayTeam?.toGameTeam()
    val homeTeam = game.homeTeam?.toGameTeam()
    val isTeamValid = homeTeam != null && awayTeam != null

    val gameId = game.gameId
    val gameStatus = game.gameStatus
    val isLeaderValid = game.pointsLeaders != null
    val isGameValid = gameId != null && gameStatus != null && isLeaderValid

    if (!isDateValid || !isTeamValid || !isGameValid) return null

    val leaders = game.pointsLeaders
        .getOrError()
        .mapNotNull { leader ->
            leader?.toPointsLeader()
        }

    val gameStatusText = game.gameStatusText.getOrNA()

    return Game(
        homeTeamId = homeTeam.getOrError().team.teamId,
        awayTeamId = awayTeam.getOrError().team.teamId,
        awayTeam = awayTeam.getOrError(),
        gameId = gameId.getOrError(),
        gameStatus = gameStatus.getOrError(),
        gameStatusText = gameStatusText,
        homeTeam = homeTeam.getOrError(),
        gameDate = gameDate.getOrError(),
        gameDateTime = gameDateTime.getOrError(),
        pointsLeaders = leaders,
        gameLeaders = null,
        teamLeaders = null
    )
}
