package com.jiachian.nbatoday.compose.screen.score.data

import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel

data class ScoreTeamRowData(
    val homeValue: String,
    val awayValue: String,
    val label: ScoreTeamLabel
)
