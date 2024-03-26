package com.jiachian.nbatoday.compose.screen.player.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting

@Stable
interface PlayerStatsState {
    val data: List<PlayerStatsRowData>
    val sorting: PlayerStatsSorting
}

class MutablePlayerStatsState : PlayerStatsState {
    override var data: List<PlayerStatsRowData> by mutableStateOf(emptyList())
    override var sorting: PlayerStatsSorting by mutableStateOf(PlayerStatsSorting.TIME_FRAME)
}
