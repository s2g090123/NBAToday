package com.jiachian.nbatoday.bet.ui.turntable.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class TurnTableStatus {
    object Idle : TurnTableStatus()
    class Asking(val win: Win, val lose: Lose) : TurnTableStatus()
    class Rewarded(val points: Long) : TurnTableStatus()
    class TurnTable(val win: Win, val lose: Lose) : TurnTableStatus() {
        var running by mutableStateOf(false)
        var angle by mutableStateOf(0f)
    }
}
