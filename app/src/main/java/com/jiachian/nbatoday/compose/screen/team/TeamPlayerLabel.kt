package com.jiachian.nbatoday.compose.screen.team

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.R

enum class TeamPlayerLabel(
    val width: Dp,
    @StringRes val textRes: Int,
    val sort: PlayerSort,
) {
    GP(
        width = 40.dp,
        textRes = R.string.player_stats_label_gp,
        sort = PlayerSort.GP
    ),
    WIN(
        width = 40.dp,
        textRes = R.string.player_stats_label_w,
        sort = PlayerSort.W
    ),
    LOSE(
        width = 40.dp,
        textRes = R.string.player_stats_label_l,
        sort = PlayerSort.L
    ),
    WINP(
        width = 64.dp,
        textRes = R.string.player_stats_label_winPercentage,
        sort = PlayerSort.WINP
    ),
    PTS(
        width = 64.dp,
        textRes = R.string.player_stats_label_pts,
        sort = PlayerSort.PTS
    ),
    FGM(
        width = 64.dp,
        textRes = R.string.player_stats_label_fgm,
        sort = PlayerSort.FGM
    ),
    FGA(
        width = 64.dp,
        textRes = R.string.player_stats_label_fga,
        sort = PlayerSort.FGA
    ),
    FGP(
        width = 64.dp,
        textRes = R.string.player_stats_label_fgPercentage,
        sort = PlayerSort.FGP
    ),
    PM3(
        width = 64.dp,
        textRes = R.string.player_stats_label_3pm,
        sort = PlayerSort.PM3
    ),
    PA3(
        width = 64.dp,
        textRes = R.string.player_stats_label_3pa,
        sort = PlayerSort.PA3
    ),
    PP3(
        width = 64.dp,
        textRes = R.string.player_stats_label_3pPercentage,
        sort = PlayerSort.PP3
    ),
    FTM(
        width = 64.dp,
        textRes = R.string.player_stats_label_ftm,
        sort = PlayerSort.FTM
    ),
    FTA(
        width = 64.dp,
        textRes = R.string.player_stats_label_fta,
        sort = PlayerSort.FTA
    ),
    FTP(
        width = 64.dp,
        textRes = R.string.player_stats_label_ftPercentage,
        sort = PlayerSort.FTP
    ),
    OREB(
        width = 48.dp,
        textRes = R.string.player_stats_label_oreb,
        sort = PlayerSort.OREB
    ),
    DREB(
        width = 48.dp,
        textRes = R.string.player_stats_label_dreb,
        sort = PlayerSort.DREB
    ),
    REB(
        width = 48.dp,
        textRes = R.string.player_stats_label_reb,
        sort = PlayerSort.REB
    ),
    AST(
        width = 48.dp,
        textRes = R.string.player_stats_label_ast,
        sort = PlayerSort.AST
    ),
    TOV(
        width = 48.dp,
        textRes = R.string.player_stats_label_tov,
        sort = PlayerSort.TOV
    ),
    STL(
        width = 48.dp,
        textRes = R.string.player_stats_label_stl,
        sort = PlayerSort.STL
    ),
    BLK(
        width = 48.dp,
        textRes = R.string.player_stats_label_blk,
        sort = PlayerSort.BLK
    ),
    PF(
        width = 48.dp,
        textRes = R.string.player_stats_label_pf,
        sort = PlayerSort.PF
    ),
    PLUSMINUS(
        width = 48.dp,
        textRes = R.string.player_stats_label_plusMinus,
        sort = PlayerSort.PLUSMINUS
    ),
}
