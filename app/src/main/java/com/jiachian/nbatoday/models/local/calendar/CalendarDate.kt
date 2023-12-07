package com.jiachian.nbatoday.models.local.calendar

import java.util.Date

data class CalendarDate(
    val date: Date,
    val day: Int,
    val isCurrentMonth: Boolean
)
