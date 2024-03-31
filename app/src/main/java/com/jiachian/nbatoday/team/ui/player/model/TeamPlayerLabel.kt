package com.jiachian.nbatoday.team.ui.player.model

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class TeamPlayerLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    val sorting: TeamPlayerSorting,
) {
    GP(
        width = 40.dp,
        textRes = R.string.stats_label_gp,
        sorting = TeamPlayerSorting.GP
    ),
    W(
        width = 40.dp,
        textRes = R.string.stats_label_w,
        sorting = TeamPlayerSorting.W
    ),
    L(
        width = 40.dp,
        textRes = R.string.stats_label_l,
        sorting = TeamPlayerSorting.L
    ),
    WINP(
        width = 64.dp,
        textRes = R.string.stats_label_winPercentage,
        sorting = TeamPlayerSorting.WINP
    ),
    PTS(
        width = 64.dp,
        textRes = R.string.stats_label_pts,
        sorting = TeamPlayerSorting.PTS
    ),
    FGM(
        width = 64.dp,
        textRes = R.string.stats_label_fgm,
        sorting = TeamPlayerSorting.FGM
    ),
    FGA(
        width = 64.dp,
        textRes = R.string.stats_label_fga,
        sorting = TeamPlayerSorting.FGA
    ),
    FGP(
        width = 64.dp,
        textRes = R.string.stats_label_fgPercentage,
        sorting = TeamPlayerSorting.FGP
    ),
    PM3(
        width = 64.dp,
        textRes = R.string.stats_label_3pm,
        sorting = TeamPlayerSorting.PM3
    ),
    PA3(
        width = 64.dp,
        textRes = R.string.stats_label_3pa,
        sorting = TeamPlayerSorting.PA3
    ),
    PP3(
        width = 64.dp,
        textRes = R.string.stats_label_3pPercentage,
        sorting = TeamPlayerSorting.PP3
    ),
    FTM(
        width = 64.dp,
        textRes = R.string.stats_label_ftm,
        sorting = TeamPlayerSorting.FTM
    ),
    FTA(
        width = 64.dp,
        textRes = R.string.stats_label_fta,
        sorting = TeamPlayerSorting.FTA
    ),
    FTP(
        width = 64.dp,
        textRes = R.string.stats_label_ftPercentage,
        sorting = TeamPlayerSorting.FTP
    ),
    OREB(
        width = 48.dp,
        textRes = R.string.stats_label_oreb,
        sorting = TeamPlayerSorting.OREB
    ),
    DREB(
        width = 48.dp,
        textRes = R.string.stats_label_dreb,
        sorting = TeamPlayerSorting.DREB
    ),
    REB(
        width = 48.dp,
        textRes = R.string.stats_label_reb,
        sorting = TeamPlayerSorting.REB
    ),
    AST(
        width = 48.dp,
        textRes = R.string.stats_label_ast,
        sorting = TeamPlayerSorting.AST
    ),
    TOV(
        width = 48.dp,
        textRes = R.string.stats_label_tov,
        sorting = TeamPlayerSorting.TOV
    ),
    STL(
        width = 48.dp,
        textRes = R.string.stats_label_stl,
        sorting = TeamPlayerSorting.STL
    ),
    BLK(
        width = 48.dp,
        textRes = R.string.stats_label_blk,
        sorting = TeamPlayerSorting.BLK
    ),
    PF(
        width = 48.dp,
        textRes = R.string.stats_label_pf,
        sorting = TeamPlayerSorting.PF
    ),
    PLUSMINUS(
        width = 48.dp,
        textRes = R.string.stats_label_plusMinus,
        sorting = TeamPlayerSorting.PLUSMINUS
    ),
}
