package com.jiachian.nbatoday.models.local.bet

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameStatus

data class BetAndGame(
    @Embedded val bet: Bet,
    @Relation(
        parentColumn = "bet_game_id",
        entityColumn = "game_id"
    )
    val game: Game
) {
    val isGameFinal
        get() = game.isGameFinal

    val isGamePlayed
        get() = game.isGamePlayed

    val isHomeTeamWin
        get() = game.isHomeTeamWin

    val isAwayTeamWin
        get() = game.isAwayTeamWin

    fun getHomePointsText(): String {
        return when {
            !isGameFinal -> "${bet.homePoints}"
            isHomeTeamWin -> "+${bet.homePoints * 2}"
            else -> "-${bet.homePoints}"
        }
    }

    fun getAwayPointsText(): String {
        return when {
            !isGameFinal -> "${bet.awayPoints}"
            !isHomeTeamWin -> "+${bet.awayPoints * 2}"
            else -> "-${bet.awayPoints}"
        }
    }

    @Composable
    fun getHomePointsTextColor(): Color {
        return when {
            !isGameFinal -> MaterialTheme.colors.primary
            isHomeTeamWin -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.secondaryVariant
        }
    }

    @Composable
    fun getAwayPointsTextColor(): Color {
        return when {
            !isGameFinal -> MaterialTheme.colors.primary
            !isHomeTeamWin -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.secondaryVariant
        }
    }

    fun getBetStatusText(): String {
        return when (game.gameStatus) {
            GameStatus.COMING_SOON -> game.gameStatusText.replaceFirst(" ", "\n") + "\n1:1"
            GameStatus.PLAYING -> "1:1"
            GameStatus.FINAL -> game.gameStatusText + "\n1:1"
        }.trim()
    }

    fun getWonPoints(): Long {
        return if (isHomeTeamWin) bet.homePoints else bet.awayPoints
    }

    fun getLostPoints(): Long {
        return if (isHomeTeamWin) bet.awayPoints else bet.homePoints
    }
}
