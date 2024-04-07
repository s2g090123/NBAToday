package com.jiachian.nbatoday.calendar.ui.state

import androidx.compose.runtime.Stable

@Stable
interface CalendarState {
    val topBar: CalendarTopBarState
    val dates: CalendarDatesState
    val games: CalendarGamesState
}

class MutableCalendarState : CalendarState {
    override val topBar: MutableCalendarTopBarState = MutableCalendarTopBarState()
    override val dates: MutableCalendarDatesState = MutableCalendarDatesState()
    override val games: MutableCalendarGamesState = MutableCalendarGamesState()
}
