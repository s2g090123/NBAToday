package com.jiachian.nbatoday.bet.ui.main.event

import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.bet.ui.turntable.model.Lose
import com.jiachian.nbatoday.bet.ui.turntable.model.Win

sealed class BetUIEvent {
    class Settle(val betGame: BetAndGame) : BetUIEvent()
    class OpenTurnTable(val win: Win, val lose: Lose) : BetUIEvent()
    class StartTurnTable(val win: Win, val lose: Lose) : BetUIEvent()
    object CloseTurnTable : BetUIEvent()
    object EventReceived : BetUIEvent()
}
