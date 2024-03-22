package com.jiachian.nbatoday.compose.screen.bet.event

import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.models.local.bet.BetAndGame

sealed class BetUIEvent {
    class Settle(val betGame: BetAndGame) : BetUIEvent()
    class OpenTurnTable(val win: Win, val lose: Lose) : BetUIEvent()
    class StartTurnTable(val win: Win, val lose: Lose) : BetUIEvent()
    object CloseTurnTable : BetUIEvent()
    object EventReceived : BetUIEvent()
}
