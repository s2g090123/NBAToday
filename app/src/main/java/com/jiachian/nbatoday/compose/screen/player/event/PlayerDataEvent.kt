package com.jiachian.nbatoday.compose.screen.player.event

import com.jiachian.nbatoday.compose.screen.player.PlayerError

sealed class PlayerDataEvent {
    class Error(val error: PlayerError) : PlayerDataEvent()
}
