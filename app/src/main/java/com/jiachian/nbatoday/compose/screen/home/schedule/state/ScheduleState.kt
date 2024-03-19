package com.jiachian.nbatoday.compose.screen.home.schedule.state

import com.jiachian.nbatoday.compose.screen.card.GameCardState
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

data class ScheduleState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val selectedDate: DateData = DateData(),
    val dates: List<DateData> = emptyList(),
    private val games: Map<DateData, List<GameCardState>> = emptyMap(),
) {
    fun getGames(date: DateData): List<GameCardState> {
        return games[date] ?: emptyList()
    }
}
