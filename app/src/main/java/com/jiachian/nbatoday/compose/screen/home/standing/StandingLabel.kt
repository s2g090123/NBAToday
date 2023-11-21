package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class StandingLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: StandingSort
)
