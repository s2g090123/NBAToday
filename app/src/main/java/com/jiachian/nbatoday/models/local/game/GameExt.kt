package com.jiachian.nbatoday.models.local.game

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
