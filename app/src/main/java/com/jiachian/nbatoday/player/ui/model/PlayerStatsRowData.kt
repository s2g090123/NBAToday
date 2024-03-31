package com.jiachian.nbatoday.player.ui.model

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.player.data.model.local.Player

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
