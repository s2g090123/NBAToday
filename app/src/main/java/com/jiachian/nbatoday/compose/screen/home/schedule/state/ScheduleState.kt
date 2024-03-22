package com.jiachian.nbatoday.compose.screen.home.schedule.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.card.models.GameCardData
import com.jiachian.nbatoday.compose.screen.home.schedule.event.ScheduleDataEvent
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

@Stable
interface ScheduleState {
    val games: Map<DateData, List<GameCardData>>
    val dates: List<DateData>
    val loading: Boolean
    val refreshing: Boolean
    val event: ScheduleDataEvent?

    fun getGames(date: DateData): List<GameCardData> {
        return games[date] ?: emptyList()
    }
}

class MutableScheduleState : ScheduleState {
    override var games: Map<DateData, List<GameCardData>> by mutableStateOf(emptyMap())
    override var dates: List<DateData> by mutableStateOf(emptyList())
    override var loading: Boolean by mutableStateOf(false)
    override var refreshing: Boolean by mutableStateOf(false)
    override var event: ScheduleDataEvent? by mutableStateOf(null)
}
