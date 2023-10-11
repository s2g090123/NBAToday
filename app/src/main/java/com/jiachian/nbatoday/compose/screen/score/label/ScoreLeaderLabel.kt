package com.jiachian.nbatoday.compose.screen.score.label

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class ScoreLeaderLabel(
    @StringRes val textRes: Int,
    val topMargin: Boolean,
    val divider: Boolean
) {
    NAME(
        textRes = R.string.box_score_leader_statistics_name,
        topMargin = true,
        divider = false
    ),
    POSITION(
        textRes = R.string.box_score_leader_statistics_position,
        topMargin = true,
        divider = false
    ),
    MIN(
        textRes = R.string.box_score_leader_statistics_time,
        topMargin = true,
        divider = true
    ),
    PTS(
        textRes = R.string.box_score_leader_statistics_points,
        topMargin = false,
        divider = false
    ),
    PLUSMINUS(
        textRes = R.string.box_score_leader_statistics_plusMinusPoints,
        topMargin = true,
        divider = false
    ),
    FG(
        textRes = R.string.box_score_leader_statistics_fieldGoal,
        topMargin = true,
        divider = false
    ),
    P2(
        textRes = R.string.box_score_leader_statistics_twoPoints,
        topMargin = true,
        divider = false
    ),
    P3(
        textRes = R.string.box_score_leader_statistics_threePoints,
        topMargin = true,
        divider = false
    ),
    FT(
        textRes = R.string.box_score_leader_statistics_freeThrows,
        topMargin = true,
        divider = true
    ),
    REB(
        textRes = R.string.box_score_leader_statistics_rebounds,
        topMargin = false,
        divider = false
    ),
    DREB(
        textRes = R.string.box_score_leader_statistics_reboundsDef,
        topMargin = true,
        divider = false
    ),
    OREB(
        textRes = R.string.box_score_leader_statistics_reboundsOff,
        topMargin = true,
        divider = false
    ),
    AST(
        textRes = R.string.box_score_leader_statistics_assists,
        topMargin = true,
        divider = false
    ),
    BLK(
        textRes = R.string.box_score_leader_statistics_blocks,
        topMargin = true,
        divider = false
    ),
    STL(
        textRes = R.string.box_score_leader_statistics_steals,
        topMargin = true,
        divider = false
    ),
    TO(
        textRes = R.string.box_score_leader_statistics_turnovers,
        topMargin = true,
        divider = false
    ),
    PF(
        textRes = R.string.box_score_leader_statistics_foulsPersonal,
        topMargin = true,
        divider = false
    ),
    TF(
        textRes = R.string.box_score_leader_statistics_foulsTechnical,
        topMargin = true,
        divider = false
    )
}
