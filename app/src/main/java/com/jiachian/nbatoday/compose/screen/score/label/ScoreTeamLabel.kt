package com.jiachian.nbatoday.compose.screen.score.label

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class ScoreTeamLabel(
    @StringRes val textRes: Int,
    val topMargin: Boolean,
    val divider: Boolean
) {
    PTS(
        textRes = R.string.box_score_statistics_points,
        topMargin = true,
        divider = false
    ),
    FG(
        textRes = R.string.box_score_statistics_fieldGoal,
        topMargin = true,
        divider = false
    ),
    TP(
        textRes = R.string.box_score_statistics_twoPoints,
        topMargin = true,
        divider = false
    ),
    P3(
        textRes = R.string.box_score_statistics_threePoints,
        topMargin = true,
        divider = false
    ),
    FT(
        textRes = R.string.box_score_statistics_freeThrows,
        topMargin = true,
        divider = true
    ),
    REB(
        textRes = R.string.box_score_statistics_rebounds,
        topMargin = false,
        divider = false
    ),
    DREB(
        textRes = R.string.box_score_statistics_reboundsDef,
        topMargin = false,
        divider = false
    ),
    OREB(
        textRes = R.string.box_score_statistics_reboundsOff,
        topMargin = false,
        divider = false
    ),
    AST(
        textRes = R.string.box_score_statistics_assists,
        topMargin = true,
        divider = false
    ),
    BLK(
        textRes = R.string.box_score_statistics_blocks,
        topMargin = true,
        divider = false
    ),
    STL(
        textRes = R.string.box_score_statistics_steals,
        topMargin = true,
        divider = false
    ),
    TO(
        textRes = R.string.box_score_statistics_turnovers,
        topMargin = true,
        divider = false
    ),
    FASTBREAK(
        textRes = R.string.box_score_statistics_pointsFastBreak,
        topMargin = true,
        divider = false
    ),
    POINTSTURNOVERS(
        textRes = R.string.box_score_statistics_pointsFromTurnOvers,
        topMargin = false,
        divider = false
    ),
    POINTSINPAINT(
        textRes = R.string.box_score_statistics_pointsInPaint,
        topMargin = false,
        divider = false
    ),
    POINTSSECONDCHANCE(
        textRes = R.string.box_score_statistics_pointsSecondChance,
        topMargin = false,
        divider = false
    ),
    BENCHPOINTS(
        textRes = R.string.box_score_statistics_benchPoints,
        topMargin = false,
        divider = false
    ),
    PF(
        textRes = R.string.box_score_statistics_foulsPersonal,
        topMargin = true,
        divider = false
    ),
    TF(
        textRes = R.string.box_score_statistics_foulsTechnical,
        topMargin = false,
        divider = false
    )
}
