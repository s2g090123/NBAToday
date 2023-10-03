package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class CareerStatsRowData(
    val value: String,
    val isFocus: Boolean,
    val textWidth: Dp,
    val textAlign: TextAlign
)
