package com.jiachian.nbatoday.compose.screen.bet.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class TurnTableUIState {
    object Idle : TurnTableUIState()
    class Asking(val win: Win, val lose: Lose) : TurnTableUIState()
    class TurnTable(val win: Win, val lose: Lose) : TurnTableUIState() {
        var running by mutableStateOf(false)
        var angle by mutableStateOf(0f)
    }

    class Rewarded(val points: Long) : TurnTableUIState()
}
