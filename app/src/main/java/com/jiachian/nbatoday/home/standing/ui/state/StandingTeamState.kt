package com.jiachian.nbatoday.home.standing.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.home.standing.ui.model.StandingRowData
import com.jiachian.nbatoday.home.standing.ui.model.StandingSorting

@Stable
interface StandingTeamState {
    val teams: List<StandingRowData>
    val sorting: StandingSorting
    val loading: Boolean
}

class MutableStandingTeamState : StandingTeamState {
    override var teams: List<StandingRowData> by mutableStateOf(emptyList())
    override var sorting: StandingSorting by mutableStateOf(StandingSorting.WINP)
    override var loading: Boolean by mutableStateOf(false)
}
