package com.jiachian.nbatoday.compose.screen.bet.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class TurnTableState {
    object Idle : TurnTableState()

    data class Asking(val win: Win, val lose: Lose) : TurnTableState()

    data class TurnTable(val win: Win, val lose: Lose) : TurnTableState() {
        var running by mutableStateOf(false)
        var angle by mutableStateOf(0f)
    }

    data class Rewarded(val points: Long) : TurnTableState()
}
