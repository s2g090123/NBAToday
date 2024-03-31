package com.jiachian.nbatoday.bet.ui.turntable.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus

@Stable
interface TurnTableState {
    val status: TurnTableStatus
}

class MutableTurnTableState : TurnTableState {
    override var status: TurnTableStatus by mutableStateOf(TurnTableStatus.Idle)
}
