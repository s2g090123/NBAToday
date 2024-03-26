package com.jiachian.nbatoday.compose.screen.player.event

import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting

sealed class PlayerUIEvent {
    class Sort(val sorting: PlayerStatsSorting) : PlayerUIEvent()
    object EventReceived : PlayerUIEvent()
}
