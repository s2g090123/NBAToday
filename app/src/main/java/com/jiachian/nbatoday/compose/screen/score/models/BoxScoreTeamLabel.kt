package com.jiachian.nbatoday.compose.screen.score.models

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class BoxScoreTeamLabel(
    @StringRes val textRes: Int,
    val topMargin: Boolean,
    val bottomDivider: Boolean
) {
    PTS(
        textRes = R.string.box_score_statistics_points,
        topMargin = true,
        bottomDivider = false
    ),
    FG(
        textRes = R.string.box_score_statistics_fieldGoal,
        topMargin = true,
        bottomDivider = false
    ),
    TP(
        textRes = R.string.box_score_statistics_twoPoints,
        topMargin = true,
        bottomDivider = false
    ),
    P3(
        textRes = R.string.box_score_statistics_threePoints,
        topMargin = true,
        bottomDivider = false
    ),
    FT(
        textRes = R.string.box_score_statistics_freeThrows,
        topMargin = true,
        bottomDivider = true
    ),
    REB(
        textRes = R.string.box_score_statistics_rebounds,
        topMargin = false,
        bottomDivider = false
    ),
    DREB(
        textRes = R.string.box_score_statistics_reboundsDef,
        topMargin = false,
        bottomDivider = false
    ),
    OREB(
        textRes = R.string.box_score_statistics_reboundsOff,
        topMargin = false,
        bottomDivider = false
    ),
    AST(
        textRes = R.string.box_score_statistics_assists,
        topMargin = true,
        bottomDivider = false
    ),
    BLK(
        textRes = R.string.box_score_statistics_blocks,
        topMargin = true,
        bottomDivider = false
    ),
    STL(
        textRes = R.string.box_score_statistics_steals,
        topMargin = true,
        bottomDivider = false
    ),
    TO(
        textRes = R.string.box_score_statistics_turnovers,
        topMargin = true,
        bottomDivider = false
    ),
    FASTBREAK(
        textRes = R.string.box_score_statistics_pointsFastBreak,
        topMargin = true,
        bottomDivider = false
    ),
    POINTSTURNOVERS(
        textRes = R.string.box_score_statistics_pointsFromTurnOvers,
        topMargin = false,
        bottomDivider = false
    ),
    POINTSINPAINT(
        textRes = R.string.box_score_statistics_pointsInPaint,
        topMargin = false,
        bottomDivider = false
    ),
    POINTSSECONDCHANCE(
        textRes = R.string.box_score_statistics_pointsSecondChance,
        topMargin = false,
        bottomDivider = false
    ),
    BENCHPOINTS(
        textRes = R.string.box_score_statistics_benchPoints,
        topMargin = false,
        bottomDivider = false
    ),
    PF(
        textRes = R.string.box_score_statistics_foulsPersonal,
        topMargin = true,
        bottomDivider = false
    ),
    TF(
        textRes = R.string.box_score_statistics_foulsTechnical,
        topMargin = false,
        bottomDivider = false
    )
}
