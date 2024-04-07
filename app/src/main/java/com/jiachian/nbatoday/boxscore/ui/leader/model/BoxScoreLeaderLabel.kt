package com.jiachian.nbatoday.boxscore.ui.leader.model

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class BoxScoreLeaderLabel(
    @StringRes val textRes: Int,
    val topMargin: Boolean,
    val bottomDivider: Boolean
) {
    NAME(
        textRes = R.string.box_score_leader_statistics_name,
        topMargin = true,
        bottomDivider = false
    ),
    POSITION(
        textRes = R.string.box_score_leader_statistics_position,
        topMargin = true,
        bottomDivider = false
    ),
    MIN(
        textRes = R.string.box_score_leader_statistics_time,
        topMargin = true,
        bottomDivider = true
    ),
    PTS(
        textRes = R.string.box_score_leader_statistics_points,
        topMargin = false,
        bottomDivider = false
    ),
    PLUSMINUS(
        textRes = R.string.box_score_leader_statistics_plusMinusPoints,
        topMargin = true,
        bottomDivider = false
    ),
    FG(
        textRes = R.string.box_score_leader_statistics_fieldGoal,
        topMargin = true,
        bottomDivider = false
    ),
    P2(
        textRes = R.string.box_score_leader_statistics_twoPoints,
        topMargin = true,
        bottomDivider = false
    ),
    P3(
        textRes = R.string.box_score_leader_statistics_threePoints,
        topMargin = true,
        bottomDivider = false
    ),
    FT(
        textRes = R.string.box_score_leader_statistics_freeThrows,
        topMargin = true,
        bottomDivider = true
    ),
    REB(
        textRes = R.string.box_score_leader_statistics_rebounds,
        topMargin = false,
        bottomDivider = false
    ),
    DREB(
        textRes = R.string.box_score_leader_statistics_reboundsDef,
        topMargin = true,
        bottomDivider = false
    ),
    OREB(
        textRes = R.string.box_score_leader_statistics_reboundsOff,
        topMargin = true,
        bottomDivider = false
    ),
    AST(
        textRes = R.string.box_score_leader_statistics_assists,
        topMargin = true,
        bottomDivider = false
    ),
    BLK(
        textRes = R.string.box_score_leader_statistics_blocks,
        topMargin = true,
        bottomDivider = false
    ),
    STL(
        textRes = R.string.box_score_leader_statistics_steals,
        topMargin = true,
        bottomDivider = false
    ),
    TO(
        textRes = R.string.box_score_leader_statistics_turnovers,
        topMargin = true,
        bottomDivider = false
    ),
    PF(
        textRes = R.string.box_score_leader_statistics_foulsPersonal,
        topMargin = true,
        bottomDivider = false
    ),
    TF(
        textRes = R.string.box_score_leader_statistics_foulsTechnical,
        topMargin = true,
        bottomDivider = false
    )
}
