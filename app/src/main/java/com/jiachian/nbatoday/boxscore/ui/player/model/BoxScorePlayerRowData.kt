package com.jiachian.nbatoday.boxscore.ui.player.model

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore

data class BoxScorePlayerRowData(
    val player: BoxScore.BoxScoreTeam.Player,
    val data: List<Data>,
) {
    data class Data(
        val value: String,
        val width: Dp,
        val align: TextAlign,
    )
}
