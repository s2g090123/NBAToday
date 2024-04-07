package com.jiachian.nbatoday.calendar.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.game.ui.model.GameCardData

@Stable
interface CalendarGamesState {
    val games: List<GameCardData>
    val loading: Boolean
    val visible: Boolean
}

class MutableCalendarGamesState : CalendarGamesState {
    override var games: List<GameCardData> by mutableStateOf(emptyList())
    override var loading: Boolean by mutableStateOf(false)
    override var visible: Boolean by mutableStateOf(false)
}
