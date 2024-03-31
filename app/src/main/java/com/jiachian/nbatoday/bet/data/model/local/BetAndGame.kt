package com.jiachian.nbatoday.bet.data.model.local

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameStatus

data class BetAndGame(
    @Embedded val bet: Bet,
    @Relation(
        parentColumn = "bet_game_id",
        entityColumn = "game_id"
    )
    val game: Game
) {
    val gameFinal
        get() = game.gameFinal

    val gamePlayed
        get() = game.gamePlayed

    val homeWin
        get() = game.homeWin

    val awayWin
        get() = game.awayWin

    /**
     * Gets the text representation of home team points, considering the game's status and result.
     */
    fun getHomePointsText(): String {
        return when {
            !gameFinal -> "${bet.homePoints}"
            homeWin -> "+${bet.homePoints * 2}"
            else -> "-${bet.homePoints}"
        }
    }

    /**
     * Gets the text representation of away team points, considering the game's status and result.
     */
    fun getAwayPointsText(): String {
        return when {
            !gameFinal -> "${bet.awayPoints}"
            !homeWin -> "+${bet.awayPoints * 2}"
            else -> "-${bet.awayPoints}"
        }
    }

    /**
     * Gets the color of home team points text, based on the game's status and result.
     */
    @Composable
    fun getHomePointsTextColor(): Color {
        return when {
            !gameFinal -> MaterialTheme.colors.primary
            homeWin -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.secondaryVariant
        }
    }

    /**
     * Gets the color of away team points text, based on the game's status and result.
     */
    @Composable
    fun getAwayPointsTextColor(): Color {
        return when {
            !gameFinal -> MaterialTheme.colors.primary
            !homeWin -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.secondaryVariant
        }
    }

    /**
     * Gets the text representation of the bet status based on the associated game's status.
     */
    fun getBetStatusText(): String {
        return when (game.gameStatus) {
            GameStatus.COMING_SOON -> game.gameStatusText.replaceFirst(" ", "\n") + "\n1:1"
            GameStatus.PLAYING -> "1:1"
            GameStatus.FINAL -> game.gameStatusText + "\n1:1"
        }.trim()
    }

    /**
     * Gets the number of points won in the bet.
     */
    fun getWonPoints(): Long {
        return if (homeWin) bet.homePoints else bet.awayPoints
    }

    /**
     * Gets the number of points lost in the bet.
     */
    fun getLostPoints(): Long {
        return if (homeWin) bet.awayPoints else bet.homePoints
    }
}
