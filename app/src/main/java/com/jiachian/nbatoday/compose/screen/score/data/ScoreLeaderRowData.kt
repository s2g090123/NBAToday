package com.jiachian.nbatoday.compose.screen.score.data

import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel

data class ScoreLeaderRowData(
    val homeValue: String,
    val awayValue: String,
    val label: ScoreLeaderLabel
)
