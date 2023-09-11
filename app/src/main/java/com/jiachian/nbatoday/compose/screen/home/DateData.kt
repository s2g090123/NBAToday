package com.jiachian.nbatoday.compose.screen.home

data class DateData(
    val year: Int,
    val month: Int,
    val day: Int
) {
    val dateString = "$year/$month/$day"
    val monthAndDay = "$month/$day"
}
