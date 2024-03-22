package com.jiachian.nbatoday.compose.screen.home.schedule.state

import com.jiachian.nbatoday.compose.screen.card.models.GameCardData
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

data class ScheduleState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    private val games: Map<DateData, List<GameCardData>> = emptyMap(),
) {
    fun getGames(date: DateData): List<GameCardData> {
        return games[date] ?: emptyList()
    }
}
