package com.jiachian.nbatoday.compose.screen.calendar.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface CalendarTopBarState {
    val index: Int
    val dateString: String
    val hasPrevious: Boolean
    val hasNext: Boolean
}

class MutableCalendarTopBarState : CalendarTopBarState {
    override var index: Int by mutableStateOf(0)
    override var dateString: String by mutableStateOf("")
    override var hasPrevious: Boolean by mutableStateOf(false)
    override var hasNext: Boolean by mutableStateOf(false)
}
