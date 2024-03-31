package com.jiachian.nbatoday.calendar.ui.model

import java.util.Date

data class CalendarDate(
    val date: Date,
    val day: Int,
    val currentMonth: Boolean
)
