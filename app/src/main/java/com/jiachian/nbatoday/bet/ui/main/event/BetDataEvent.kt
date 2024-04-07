package com.jiachian.nbatoday.bet.ui.main.event

import com.jiachian.nbatoday.bet.ui.main.error.BetError

sealed class BetDataEvent {
    class Error(val error: BetError) : BetDataEvent()
}
