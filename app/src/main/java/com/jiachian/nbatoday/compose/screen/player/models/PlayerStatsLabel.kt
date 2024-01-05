package com.jiachian.nbatoday.compose.screen.player.models

import androidx.annotation.StringRes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class PlayerStatsLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    val align: TextAlign,
    val sorting: PlayerStatsSorting,
) {
    GP(
        width = 40.dp,
        textRes = R.string.stats_label_gp,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.GP,
    ),
    W(
        width = 40.dp,
        textRes = R.string.stats_label_w,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.W,
    ),
    L(
        width = 40.dp,
        textRes = R.string.stats_label_l,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.L,
    ),
    WINP(
        width = 64.dp,
        textRes = R.string.stats_label_winPercentage,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.WINP,
    ),
    PTS(
        width = 64.dp,
        textRes = R.string.stats_label_pts,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PTS,
    ),
    FGM(
        width = 64.dp,
        textRes = R.string.stats_label_fgm,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FGM,
    ),
    FGA(
        width = 64.dp,
        textRes = R.string.stats_label_fga,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FGA,
    ),
    FGP(
        width = 64.dp,
        textRes = R.string.stats_label_fgPercentage,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FGP,
    ),
    PM3(
        width = 64.dp,
        textRes = R.string.stats_label_3pm,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PM3,
    ),
    PA3(
        width = 64.dp,
        textRes = R.string.stats_label_3pa,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PA3,
    ),
    PP3(
        width = 64.dp,
        textRes = R.string.stats_label_3pPercentage,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PP3,
    ),
    FTM(
        width = 64.dp,
        textRes = R.string.stats_label_ftm,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FTM,
    ),
    FTA(
        width = 64.dp,
        textRes = R.string.stats_label_fta,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FTA,
    ),
    FTP(
        width = 64.dp,
        textRes = R.string.stats_label_ftPercentage,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.FTP,
    ),
    OREB(
        width = 48.dp,
        textRes = R.string.stats_label_oreb,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.OREB,
    ),
    DREB(
        width = 48.dp,
        textRes = R.string.stats_label_dreb,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.DREB,
    ),
    REB(
        width = 48.dp,
        textRes = R.string.stats_label_reb,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.REB,
    ),
    AST(
        width = 48.dp,
        textRes = R.string.stats_label_ast,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.AST,
    ),
    TOV(
        width = 48.dp,
        textRes = R.string.stats_label_tov,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.TOV,
    ),
    STL(
        width = 48.dp,
        textRes = R.string.stats_label_stl,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.STL,
    ),
    BLK(
        width = 48.dp,
        textRes = R.string.stats_label_blk,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.BLK,
    ),
    PF(
        width = 48.dp,
        textRes = R.string.stats_label_pf,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PF,
    ),
    PLUSMINUS(
        width = 48.dp,
        textRes = R.string.stats_label_plusMinus,
        align = TextAlign.End,
        sorting = PlayerStatsSorting.PLUSMINUS,
    ),
}
