package com.jiachian.nbatoday.player.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.player.ui.event.PlayerDataEvent

@Stable
interface PlayerState {
    val info: PlayerInfoState
    val stats: PlayerStatsState
    val loading: Boolean
    val notFound: Boolean
    val event: PlayerDataEvent?
}

class MutablePlayerState(
    playerId: Int
) : PlayerState {
    override val info: MutablePlayerInfoState = MutablePlayerInfoState(playerId)
    override val stats: MutablePlayerStatsState = MutablePlayerStatsState()
    override var loading: Boolean by mutableStateOf(false)
    override var notFound: Boolean by mutableStateOf(false)
    override var event: PlayerDataEvent? by mutableStateOf(null)
}
