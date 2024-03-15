package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.models.local.bet.BetAndGame

sealed class BetEvent {
    data class Settle(val betGame: BetAndGame) : BetEvent()
    object CloseTurnTable : BetEvent()
    data class OpenTurnTable(val win: Win, val lose: Lose) : BetEvent()
    data class StartTurnTable(val win: Win, val lose: Lose) : BetEvent()
}
