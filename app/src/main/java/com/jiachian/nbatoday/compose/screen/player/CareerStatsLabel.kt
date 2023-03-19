package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class CareerStatsLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: CareerStatsSort?
)