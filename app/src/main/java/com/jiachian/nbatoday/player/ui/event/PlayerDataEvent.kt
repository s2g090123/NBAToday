package com.jiachian.nbatoday.player.ui.event

import com.jiachian.nbatoday.player.ui.error.PlayerError

sealed class PlayerDataEvent {
    class Error(val error: PlayerError) : PlayerDataEvent()
}
