package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.data.local.player.PlayerCareer

data class CareerStatsLabel(
    val textWidth: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: CareerStatsSort
) {
    fun getRowData(
        stats: PlayerCareer.PlayerCareerStats.Stats,
        sorting: CareerStatsSort,
    ): CareerStatsRowData {
        val value = when (text) {
            "GP" -> stats.gamePlayed.toString()
            "W" -> stats.win.toString()
            "L" -> stats.lose.toString()
            "WIN%" -> stats.winPercentage.toString()
            "PTS" -> (stats.points.toDouble() / stats.gamePlayed).toString()
            "FGM" -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).toString()
            "FGA" -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).toString()
            "FG%" -> stats.fieldGoalsPercentage.toString()
            "3PM" -> (stats.threePointersMade.toDouble() / stats.gamePlayed).toString()
            "3PA" -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).toString()
            "3P%" -> stats.threePointersPercentage.toString()
            "FTM" -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).toString()
            "FTA" -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).toString()
            "FT%" -> stats.freeThrowsPercentage.toString()
            "OREB" -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).toString()
            "DREB" -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).toString()
            "REB" -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).toString()
            "AST" -> (stats.assists.toDouble() / stats.gamePlayed).toString()
            "TOV" -> (stats.turnovers.toDouble() / stats.gamePlayed).toString()
            "STL" -> (stats.steals.toDouble() / stats.gamePlayed).toString()
            "BLK" -> (stats.blocks.toDouble() / stats.gamePlayed).toString()
            "PF" -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).toString()
            "+/-" -> stats.plusMinus.toString()
            else -> ""
        }
        return CareerStatsRowData(
            value = value,
            isFocus = sort == sorting,
            textWidth = textWidth,
            textAlign = textAlign
        )
    }
}
