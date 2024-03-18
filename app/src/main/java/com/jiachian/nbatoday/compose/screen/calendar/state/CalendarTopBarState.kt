package com.jiachian.nbatoday.compose.screen.calendar.state

data class CalendarTopBarState(
    val index: Int = 0,
    val dateString: String = "",
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
)
