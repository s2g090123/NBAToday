package com.jiachian.nbatoday.compose.screen.score.label

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class ScoreLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    @StringRes val infoRes: Int,
    val textAlign: TextAlign,
    val type: ScoreLabelType
) {
    MIN(
        width = 72.dp,
        textRes = R.string.label_min,
        textAlign = TextAlign.Center,
        infoRes = R.string.box_score_about_min,
        type = ScoreLabelType.MIN
    ),
    FGMA(
        width = 72.dp,
        textRes = R.string.label_fgm,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_FGMA,
        type = ScoreLabelType.FG
    ),
    PMA3(
        width = 72.dp,
        textRes = R.string.label_3pm,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_3PMA,
        type = ScoreLabelType.P3
    ),
    FTMA(
        width = 72.dp,
        textRes = R.string.label_ftm,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_FTMA,
        type = ScoreLabelType.FT
    ),
    PLUSMINUS(
        width = 40.dp,
        textRes = R.string.label_plus_minus,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_plusMinus,
        type = ScoreLabelType.PLUSMINUS
    ),
    OREB(
        width = 40.dp,
        textRes = R.string.label_or,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_OR,
        type = ScoreLabelType.OREB
    ),
    DREB(
        width = 40.dp,
        textRes = R.string.label_dr,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_DR,
        type = ScoreLabelType.DREB
    ),
    REB(
        width = 40.dp,
        textRes = R.string.label_tr,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_TR,
        type = ScoreLabelType.REB
    ),
    AST(
        width = 40.dp,
        textRes = R.string.label_as,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_AS,
        type = ScoreLabelType.AST
    ),
    PF(
        width = 40.dp,
        textRes = R.string.label_pf,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_PF,
        type = ScoreLabelType.PF
    ),
    STL(
        width = 40.dp,
        textRes = R.string.label_st,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_ST,
        type = ScoreLabelType.STL
    ),
    TOV(
        width = 40.dp,
        textRes = R.string.label_to,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_TO,
        type = ScoreLabelType.TOV
    ),
    BS(
        width = 40.dp,
        textRes = R.string.label_bs,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_BS,
        type = ScoreLabelType.BS
    ),
    BA(
        width = 40.dp,
        textRes = R.string.label_ba,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_BA,
        type = ScoreLabelType.BA
    ),
    PTS(
        width = 48.dp,
        textRes = R.string.label_pts,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_PTS,
        type = ScoreLabelType.PTS
    ),
    EFF(
        width = 48.dp,
        textRes = R.string.label_eff,
        textAlign = TextAlign.End,
        infoRes = R.string.box_score_about_EFF,
        type = ScoreLabelType.EFF
    )
}
