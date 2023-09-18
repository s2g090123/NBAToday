package com.jiachian.nbatoday.compose.screen.score

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

data class ScoreLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    val textAlign: TextAlign,
    @StringRes val infoRes: Int
)
