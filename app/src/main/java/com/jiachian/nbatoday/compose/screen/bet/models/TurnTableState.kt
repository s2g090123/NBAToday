package com.jiachian.nbatoday.compose.screen.bet.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface TurnTableState {
    val status: TurnTableStatus
}

class MutableTurnTableState : TurnTableState {
    override var status: TurnTableStatus by mutableStateOf(TurnTableStatus.Idle)
}

sealed class TurnTableStatus {
    object Idle : TurnTableStatus()

    class Asking(val win: Win, val lose: Lose) : TurnTableStatus()

    class TurnTable(val win: Win, val lose: Lose) : TurnTableStatus() {
        var running by mutableStateOf(false)
        var angle by mutableStateOf(0f)
    }

    class Rewarded(val points: Long) : TurnTableStatus()
}
