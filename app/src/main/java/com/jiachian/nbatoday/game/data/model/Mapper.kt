package com.jiachian.nbatoday.game.data.model

import android.annotation.SuppressLint
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.local.GameLeaders
import com.jiachian.nbatoday.game.data.model.local.GameScoreUpdateData
import com.jiachian.nbatoday.game.data.model.local.GameTeam
import com.jiachian.nbatoday.game.data.model.local.GameUpdateData
import com.jiachian.nbatoday.game.data.model.remote.GameDto
import com.jiachian.nbatoday.game.data.model.remote.ScheduleDto
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlinx.coroutines.flow.Flow

fun Game.toGameScoreUpdateData(): GameScoreUpdateData {
    return GameScoreUpdateData(
        gameId,
        gameStatus,
        gameStatusText,
        homeTeam,
        awayTeam,
        pointsLeaders
    )
}

fun List<GameAndBets>.toGameCardState(user: Flow<User?>): List<GameCardData> = map {
    GameCardData(it, user)
}

fun GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.toGameTeam(): GameTeam {
    return GameTeam(
        team = teamId.let { NBATeam.getTeamById(it) },
        losses = losses.getOrZero(),
        score = score.getOrZero(),
        wins = wins.getOrZero(),
    )
}

fun GameDto.RemoteScoreboard.toGameUpdateData(): List<GameUpdateData> {
    return games?.mapNotNull { game ->
        val gameId = game.gameId
        val gameStatus = game.getFormattedGameStatus()
        val gameStatusText = game.gameStatusText
        val homeTeam = game.homeTeam?.toGameTeam()
        val awayTeam = game.awayTeam?.toGameTeam()
        val gameLeaders = game.gameLeaders?.toGameLeaders()
        val teamLeaders = game.teamLeaders?.toGameLeaders()
        val gameNull = gameId == null || gameStatus == null || gameStatusText == null
        val teamNull = homeTeam == null || awayTeam == null
        val leaderNull = gameLeaders == null || teamLeaders == null
        if (gameNull || teamNull || leaderNull) return@mapNotNull null
        GameUpdateData(
            gameId = gameId.getOrError(),
            gameStatus = gameStatus.getOrError(),
            gameStatusText = gameStatusText.getOrError(),
            homeTeam = homeTeam.getOrError(),
            awayTeam = awayTeam.getOrError(),
            gameLeaders = gameLeaders.getOrError(),
            teamLeaders = teamLeaders.getOrError()
        )
    } ?: emptyList()
}

private fun GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.toGameLeaders():
    GameLeaders {
    return GameLeaders(
        homeLeader = homeLeader.toGameLeader(),
        awayLeader = awayLeader.toGameLeader(),
    )
}

private fun GameDto.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader.toGameLeader():
    GameLeaders.GameLeader {
    return GameLeaders.GameLeader(
        playerId = playerId,
        name = name,
        jerseyNum = jerseyNum,
        position = position,
        teamTricode = teamTricode,
        points = points,
        rebounds = rebounds,
        assists = assists,
    )
}

@SuppressLint("SimpleDateFormat")
fun ScheduleDto.RemoteLeagueSchedule.toGames(): List<Game> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("EST")
    }
    return gameDates?.mapNotNull { gameDate ->
        gameDate.games?.mapNotNull { game ->
            createGame(game, dateFormat)
        }
    }?.flatten() ?: emptyList()
}

private fun ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam.toGameTeam(): GameTeam {
    return GameTeam(
        team = teamId.let { NBATeam.getTeamById(it) },
        losses = losses.getOrZero(),
        score = score.getOrZero(),
        wins = wins.getOrZero(),
    )
}

private fun ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader.toPointsLeader():
    Game.PointsLeader? {
    if (playerId == null || teamId == null) return null
    val points = points.getOrZero()
    return Game.PointsLeader(
        playerId = playerId,
        points = points,
        teamId = teamId,
    )
}

private fun createGame(
    game: ScheduleDto.RemoteLeagueSchedule.RemoteGameDate.RemoteGame,
    dateFormat: SimpleDateFormat
): Game? {
    val gameDate = DateUtils.parseDate(game.gameDateEst, dateFormat)
    val gameDateTime = DateUtils.parseDate(game.gameDateTimeEst, dateFormat)
    val dateValid = gameDate != null && gameDateTime != null

    val awayTeam = game.awayTeam?.toGameTeam()
    val homeTeam = game.homeTeam?.toGameTeam()
    val teamValid = homeTeam != null && awayTeam != null

    val gameId = game.gameId
    val gameStatus = game.gameStatus
    val leaderValid = game.pointsLeaders != null
    val gameValid = gameId != null && gameStatus != null && leaderValid

    if (!dateValid || !teamValid || !gameValid) return null

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
