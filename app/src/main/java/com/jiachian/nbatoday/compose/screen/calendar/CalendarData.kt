package com.jiachian.nbatoday.compose.screen.calendar

import java.util.*

data class CalendarData(
    val date: Date,
    val month: Int,
    val day: Int,
    val isCurrentMonth: Boolean
)