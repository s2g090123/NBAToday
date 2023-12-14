package com.jiachian.nbatoday.compose.screen.home.standing.models

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
    FGM(
        width = 64.dp,
        textRes = R.string.stats_label_fgm,
        align = TextAlign.End,
        sorting = StandingSorting.FGM,
    ),
    FGA(
        width = 64.dp,
        textRes = R.string.stats_label_fga,
        align = TextAlign.End,
        sorting = StandingSorting.FGA,
    ),
    FGP(
        width = 64.dp,
        textRes = R.string.stats_label_fgPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.FGP,
    ),
    PM3(
        width = 64.dp,
        textRes = R.string.stats_label_3pm,
        align = TextAlign.End,
        sorting = StandingSorting.PM3,
    ),
    PA3(
        width = 64.dp,
        textRes = R.string.stats_label_3pa,
        align = TextAlign.End,
        sorting = StandingSorting.PA3,
    ),
    PP3(
        width = 64.dp,
        textRes = R.string.stats_label_3pPercentage,
        align = TextAlign.End,
        sorting = StandingSorting.PP3,
    ),
    FTM(
        width = 64.dp,
        textRes = R.string.stats_label_ftm,
        align = TextAlign.End,
        sorting = StandingSorting.FTM,
    ),
    FTA(
        width = 64.dp,
        textRes = R.string.stats_label_fta,
        align = TextAlign.End,
        sorting = StandingSorting.FTA,
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
    REB(
        width = 48.dp,
        textRes = R.string.stats_label_reb,
        align = TextAlign.End,
        sorting = StandingSorting.REB,
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
    PF(
        width = 48.dp,
        textRes = R.string.stats_label_pf,
        align = TextAlign.End,
        sorting = StandingSorting.PF,
    ),
}
