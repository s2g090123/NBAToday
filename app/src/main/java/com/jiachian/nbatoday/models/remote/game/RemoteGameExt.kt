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
    return games?.mapNotNull { game ->
        val gameId = game.gameId
        val gameStatus = game.getFormattedGameStatus()
        val gameStatusText = game.gameStatusText
        val homeTeam = game.homeTeam?.toGameTeam()
        val awayTeam = game.awayTeam?.toGameTeam()
        val gameLeaders = game.gameLeaders?.toGameLeaders()
        val teamLeaders = game.teamLeaders?.toGameLeaders()
        val isGameNull = gameId.isNull() || gameStatus.isNull() || gameStatusText.isNull()
        val isTeamNull = homeTeam.isNull() || awayTeam.isNull()
        val isLeadersNull = gameLeaders.isNull() || teamLeaders.isNull()
        if (isGameNull || isTeamNull || isLeadersNull) return@mapNotNull null
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

private fun RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.toGameLeaders(): GameLeaders {
    return GameLeaders(
        homeLeader = homeLeader.toGameLeader(),
        awayLeader = awayLeader.toGameLeader(),
    )
}

private fun RemoteGame.RemoteScoreboard.RemoteGameDetail.RemoteGameLeaders.RemoteGameLeader.toGameLeader(): GameLeaders.GameLeader {
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
