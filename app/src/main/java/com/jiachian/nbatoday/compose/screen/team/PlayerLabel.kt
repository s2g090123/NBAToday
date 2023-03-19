package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class PlayerLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: PlayerSort
)