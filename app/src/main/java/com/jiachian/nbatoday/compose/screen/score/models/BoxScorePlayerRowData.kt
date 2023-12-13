package com.jiachian.nbatoday.compose.screen.score.models

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.models.local.score.BoxScore

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
