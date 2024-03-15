package com.jiachian.nbatoday.compose.screen.bet.models

import com.jiachian.nbatoday.models.local.bet.BetAndGame

data class BetState(
    val data: List<BetAndGame> = emptyList(),
    val loading: Boolean = false,
)
