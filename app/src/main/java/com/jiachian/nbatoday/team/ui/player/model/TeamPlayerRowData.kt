package com.jiachian.nbatoday.team.ui.player.model

import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer

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
