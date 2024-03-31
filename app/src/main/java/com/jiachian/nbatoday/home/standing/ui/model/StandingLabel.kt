package com.jiachian.nbatoday.home.standing.ui.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class StandingLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    val align: TextAlign,
    val sorting: StandingSorting
) {
    GP(
        width = 40.dp,
        textRes = R.string.stats_label_gp,
        align = TextAlign.End,
        sorting = StandingSorting.GP,
    ),
    W(
        width = 40.dp,
        textRes = R.string.stats_label_w,
        align = TextAlign.End,
        sorting = StandingSorting.W,
    ),
    L(
        width = 40.dp,
        textRes = R.string.stats_label_l,
        align = TextAlign.End,
        sorting = StandingSorting.L,
    ),
    WINP(
        width = 64.dp,
        textRes = R.string.stats_label_winPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.WINP,
    ),
    PTS(
        width = 64.dp,
        textRes = R.string.stats_label_pts,
        align = TextAlign.End,
        sorting = StandingSorting.PTS,
    ),
    FGP(
        width = 64.dp,
        textRes = R.string.stats_label_fgPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.FGP,
    ),
    PP3(
        width = 64.dp,
        textRes = R.string.stats_label_3pPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.PP3,
    ),
    FTP(
        width = 64.dp,
        textRes = R.string.stats_label_ftPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.FTP,
    ),
    OREB(
        width = 48.dp,
        textRes = R.string.stats_label_oreb,
        align = TextAlign.End,
        sorting = StandingSorting.OREB,
    ),
    DREB(
        width = 48.dp,
        textRes = R.string.stats_label_dreb,
        align = TextAlign.End,
        sorting = StandingSorting.DREB,
    ),
    AST(
        width = 48.dp,
        textRes = R.string.stats_label_ast,
        align = TextAlign.End,
        sorting = StandingSorting.AST,
    ),
    TOV(
        width = 48.dp,
        textRes = R.string.stats_label_tov,
        align = TextAlign.End,
        sorting = StandingSorting.TOV,
    ),
    STL(
        width = 48.dp,
        textRes = R.string.stats_label_stl,
        align = TextAlign.End,
        sorting = StandingSorting.STL,
    ),
    BLK(
        width = 48.dp,
        textRes = R.string.stats_label_blk,
        align = TextAlign.End,
        sorting = StandingSorting.BLK,
    ),
}
