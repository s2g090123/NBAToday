package com.jiachian.nbatoday.compose.screen.bet.dialog

import com.jiachian.nbatoday.models.local.game.Game

data class BetDialogState(
    val game: Game? = null,
    val userPoints: Long = 0,
)
