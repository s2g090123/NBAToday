package com.jiachian.nbatoday.compose.screen.bet.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class TurnTableUIState {
    object Idle : TurnTableUIState()

    data class Asking(val win: Win, val lose: Lose) : TurnTableUIState()

    data class TurnTable(val win: Win, val lose: Lose) : TurnTableUIState() {
        var running by mutableStateOf(false)
        var angle by mutableStateOf(0f)
    }

    data class Rewarded(val points: Long) : TurnTableUIState()
}
