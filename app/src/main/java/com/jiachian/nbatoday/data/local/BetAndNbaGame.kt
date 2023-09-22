package com.jiachian.nbatoday.data.local

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.remote.game.GameStatusCode

data class BetAndNbaGame(
    @Embedded val bets: Bets,
    @Relation(
        parentColumn = "bets_game_id",
        entityColumn = "game_id"
    )
    val game: NbaGame
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
        return if (!isGameFinal) {
            "${bets.homePoints}"
        } else if (isHomeTeamWin) {
            "+${bets.homePoints * 2}"
        } else {
            "-${bets.homePoints}"
        }
    }

    fun getAwayPointsText(): String {
        return if (!isGameFinal) {
            "${bets.awayPoints}"
        } else if (!isHomeTeamWin) {
            "+${bets.awayPoints * 2}"
        } else {
            "-${bets.awayPoints}"
        }
    }

    @Composable
    fun getHomePointsTextColor(): Color {
        return if (!isGameFinal) {
            MaterialTheme.colors.primary
        } else if (isHomeTeamWin) {
            MaterialTheme.colors.primaryVariant
        } else {
            MaterialTheme.colors.secondaryVariant
        }
    }

    @Composable
    fun getAwayPointsTextColor(): Color {
        return if (!isGameFinal) {
            MaterialTheme.colors.primary
        } else if (!isHomeTeamWin) {
            MaterialTheme.colors.primaryVariant
        } else {
            MaterialTheme.colors.secondaryVariant
        }
    }

    fun getBetStatusText(): String {
        return when (game.gameStatus) {
            GameStatusCode.COMING_SOON -> game.gameStatusText.replaceFirst(
                " ",
                "\n"
            ) + "\n1:1"

            GameStatusCode.PLAYING -> "1:1"
            GameStatusCode.FINAL -> game.gameStatusText + "\n1:1"
        }.trim()
    }
}
