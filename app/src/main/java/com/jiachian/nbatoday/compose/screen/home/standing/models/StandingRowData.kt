package com.jiachian.nbatoday.compose.screen.home.standing.models

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.jiachian.nbatoday.models.local.team.Team

data class StandingRowData(
    val team: Team,
    val data: List<Data>,
) {
    companion object {
        const val ELIMINATED_STANDING = 10
    }

    data class Data(
        val value: String,
        val width: Dp,
        val align: TextAlign,
        val sorting: StandingSorting,
    )
}
