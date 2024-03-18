package com.jiachian.nbatoday.compose.screen.calendar.state

import com.jiachian.nbatoday.compose.screen.card.GameCardState

data class CalendarGamesState(
    val games: List<GameCardState> = emptyList(),
    val loading: Boolean = false,
    val visible: Boolean = true,
)
