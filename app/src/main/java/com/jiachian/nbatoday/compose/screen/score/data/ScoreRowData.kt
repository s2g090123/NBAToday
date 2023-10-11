package com.jiachian.nbatoday.compose.screen.score.data

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class ScoreRowData(
    val playerId: Int,
    val nameAbbr: String,
    val rowData: List<RowData>,
    val position: String,
    val notPlaying: Boolean,
    val notPlayingReason: String
) {
    data class RowData(
        val value: String,
        val textWidth: Dp,
        val textAlign: TextAlign
    )
}
