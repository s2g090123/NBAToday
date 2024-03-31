package com.jiachian.nbatoday.boxscore.ui.player.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class BoxScorePlayerLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    @StringRes val infoRes: Int,
    val align: TextAlign,
) {
    MIN(
        width = 72.dp,
        textRes = R.string.label_min,
        align = TextAlign.Center,
        infoRes = R.string.box_score_about_min,
    ),
    FGMA(
        width = 72.dp,
        textRes = R.string.label_fgm,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_FGMA,
    ),
    PMA3(
        width = 72.dp,
        textRes = R.string.label_3pm,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_3PMA,
    ),
    FTMA(
        width = 72.dp,
        textRes = R.string.label_ftm,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_FTMA,
    ),
    PLUSMINUS(
        width = 40.dp,
        textRes = R.string.label_plus_minus,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_plusMinus,
    ),
    OREB(
        width = 40.dp,
        textRes = R.string.label_or,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_OR,
    ),
    DREB(
        width = 40.dp,
        textRes = R.string.label_dr,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_DR,
    ),
    REB(
        width = 40.dp,
        textRes = R.string.label_tr,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_TR,
    ),
    AST(
        width = 40.dp,
        textRes = R.string.label_as,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_AS,
    ),
    PF(
        width = 40.dp,
        textRes = R.string.label_pf,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_PF,
    ),
    STL(
        width = 40.dp,
        textRes = R.string.label_st,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_ST,
    ),
    TOV(
        width = 40.dp,
        textRes = R.string.label_to,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_TO,
    ),
    BS(
        width = 40.dp,
        textRes = R.string.label_bs,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_BS,
    ),
    BA(
        width = 40.dp,
        textRes = R.string.label_ba,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_BA,
    ),
    PTS(
        width = 48.dp,
        textRes = R.string.label_pts,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_PTS,
    ),
    EFF(
        width = 48.dp,
        textRes = R.string.label_eff,
        align = TextAlign.End,
        infoRes = R.string.box_score_about_EFF,
    )
}
