package com.jiachian.nbatoday.compose.screen.calendar.models

import java.util.Date

data class CalendarDate(
    val date: Date,
    val day: Int,
    val currentMonth: Boolean
)
