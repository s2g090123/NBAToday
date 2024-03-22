package com.jiachian.nbatoday.compose.screen.bet.event

import com.jiachian.nbatoday.compose.screen.bet.BetError

sealed class BetDataEvent {
    class Error(val error: BetError) : BetDataEvent()
}
