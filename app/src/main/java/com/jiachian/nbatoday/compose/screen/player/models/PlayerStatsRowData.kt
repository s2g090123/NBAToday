package com.jiachian.nbatoday.compose.screen.player.models

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.models.local.player.Player

data class PlayerStatsRowData(
    val timeFrame: String,
    val teamAbbr: String,
    val stats: Player.PlayerStats.Stats,
    val data: List<Data>,
) {
    data class Data(
        val value: String,
        val width: Dp,
        val align: TextAlign,
        val sorting: PlayerStatsSorting,
    )
}
