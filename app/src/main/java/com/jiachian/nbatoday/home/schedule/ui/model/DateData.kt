package com.jiachian.nbatoday.home.schedule.ui.model

data class DateData(
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
) {
    val dateString = "$year/$month/$day"
    val monthAndDay = "$month/$day"
}
