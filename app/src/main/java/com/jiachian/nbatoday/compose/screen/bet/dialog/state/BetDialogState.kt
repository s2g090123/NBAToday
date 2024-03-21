package com.jiachian.nbatoday.compose.screen.bet.dialog.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.bet.dialog.event.BetDialogDataEvent
import com.jiachian.nbatoday.models.local.game.GameAndBets

@Stable
interface BetDialogState {
    val game: GameAndBets?
    val userPoints: Long
    val homePoints: Long
    val awayPoints: Long
    val warning: Boolean
    val valid: Boolean
    val loading: Boolean
    val event: BetDialogDataEvent?
}

class MutableBetDialogState : BetDialogState {
    override var game: GameAndBets? by mutableStateOf(null)
    override var userPoints: Long by mutableStateOf(0)
    override var homePoints: Long by mutableStateOf(0)
    override var awayPoints: Long by mutableStateOf(0)
    override var warning: Boolean by mutableStateOf(false)
    override val valid: Boolean by derivedStateOf { homePoints > 0 || awayPoints > 0 }
    override var loading: Boolean by mutableStateOf(false)
    override var event: BetDialogDataEvent? by mutableStateOf(null)
}
