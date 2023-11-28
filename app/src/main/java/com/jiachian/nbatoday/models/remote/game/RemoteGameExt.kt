package com.jiachian.nbatoday.models.remote.game

import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.isNull

fun RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameTeam.toGameTeam(): GameTeam {
    return GameTeam(
        team = teamId.let { NBATeam.getTeamById(it) },
        losses = losses.getOrZero(),
        score = score.getOrZero(),
        wins = wins.getOrZero(),
    )
}

fun RemoteGame.RemoteScoreboard.toGameUpdateData(): List<GameUpdateData> {
    val games = games ?: return emptyList()
    return games.mapNotNull { game ->
        val gameId = game.gameId
        val gameStatus = game.getFormattedGameStatus()
        val gameStatusText = game.gameStatusText
        val homeTeam = game.homeTeam?.toGameTeam()
        val awayTeam = game.awayTeam?.toGameTeam()
        val gameHomeLeaders = game.gameLeaders?.homeLeaders ?: return@mapNotNull null
        val gameAwayLeaders = game.gameLeaders.awayLeaders
        val teamHomeLeaders = game.teamLeaders?.homeLeaders ?: return@mapNotNull null
        val teamAwayLeaders = game.teamLeaders.awayLeaders
        val isGameNull = gameId.isNull() || gameStatus.isNull() || gameStatusText.isNull()
        val isTeamNull = homeTeam.isNull() || awayTeam.isNull()
        if (isGameNull || isTeamNull) {
            null
        } else {
            GameUpdateData(
                gameId = gameId.getOrError(),
                gameStatus = gameStatus.getOrError(),
                gameStatusText = gameStatusText.getOrError(),
                homeTeam = homeTeam.getOrError(),
                awayTeam = awayTeam.getOrError(),
                gameLeaders = GameLeaders(gameHomeLeaders, gameAwayLeaders),
                teamLeaders = GameLeaders(teamHomeLeaders, teamAwayLeaders)
            )
        }
    }
}
