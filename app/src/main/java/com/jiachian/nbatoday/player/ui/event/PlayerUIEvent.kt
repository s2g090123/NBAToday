package com.jiachian.nbatoday.player.ui.event

import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting

sealed class PlayerUIEvent {
    class Sort(val sorting: PlayerStatsSorting) : PlayerUIEvent()
    object EventReceived : PlayerUIEvent()
}
