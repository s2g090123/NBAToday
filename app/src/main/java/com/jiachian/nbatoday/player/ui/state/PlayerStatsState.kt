package com.jiachian.nbatoday.player.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.player.ui.model.PlayerStatsRowData
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting

@Stable
interface PlayerStatsState {
    val data: List<PlayerStatsRowData>
    val sorting: PlayerStatsSorting
}

class MutablePlayerStatsState : PlayerStatsState {
    override var data: List<PlayerStatsRowData> by mutableStateOf(emptyList())
    override var sorting: PlayerStatsSorting by mutableStateOf(PlayerStatsSorting.TIME_FRAME)
}
