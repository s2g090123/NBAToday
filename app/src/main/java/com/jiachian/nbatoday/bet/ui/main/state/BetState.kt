package com.jiachian.nbatoday.bet.ui.main.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.bet.ui.main.event.BetDataEvent
import com.jiachian.nbatoday.bet.ui.turntable.state.MutableTurnTableState
import com.jiachian.nbatoday.bet.ui.turntable.state.TurnTableState

@Stable
interface BetState {
    val games: List<BetAndGame>
    val turnTable: TurnTableState
    val loading: Boolean
    val event: BetDataEvent?
}

class MutableBetState : BetState {
    override var games: List<BetAndGame> by mutableStateOf(emptyList())
    override val turnTable: MutableTurnTableState = MutableTurnTableState()
    override var loading: Boolean by mutableStateOf(false)
    override var event: BetDataEvent? by mutableStateOf(null)
}
