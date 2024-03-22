package com.jiachian.nbatoday.compose.screen.bet.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.bet.event.BetDataEvent
import com.jiachian.nbatoday.models.local.bet.BetAndGame

@Stable
interface BetState {
    val games: List<BetAndGame>
    val loading: Boolean
    val event: BetDataEvent?
}

class MutableBetState : BetState {
    override var games: List<BetAndGame> by mutableStateOf(emptyList())
    override var loading: Boolean by mutableStateOf(false)
    override var event: BetDataEvent? by mutableStateOf(null)
}
