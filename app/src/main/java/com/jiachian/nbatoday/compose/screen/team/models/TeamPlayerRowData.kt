package com.jiachian.nbatoday.compose.screen.team.models

import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.models.local.team.TeamPlayer

data class TeamPlayerRowData(
    val player: TeamPlayer,
    val data: List<Data>,
) {
    data class Data(
        val value: String,
        val width: Dp,
        val sorting: TeamPlayerSorting,
    )
}
