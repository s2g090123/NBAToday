package com.jiachian.nbatoday.compose.screen.home

import android.text.format.DateUtils
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class HomeViewModel(
    repository: BaseRepository
) : ComposeViewModel() {

    companion object {
        private const val SCHEDULE_DATE_RANGE = 10
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private val homeIndexImp = MutableStateFlow(0)
    val homeIndex = homeIndexImp.asStateFlow()

    // Schedule
    val scheduleDates: List<String> = getDateStrings()
    private val scheduleIndexImp = MutableStateFlow(scheduleDates.size / 2)
    val scheduleIndex = scheduleIndexImp.asStateFlow()
    private val scheduleGamesImp = NbaUtils.getCalendar().let {
        it.set(Calendar.HOUR, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        repository.getGamesDuring(
            it.timeInMillis - DateUtils.DAY_IN_MILLIS * SCHEDULE_DATE_RANGE,
            it.timeInMillis + DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE + 1)
        )
    }

    val scheduleGames = scheduleGamesImp.map {
        val calendar = NbaUtils.getCalendar()
        it.groupBy { game ->
            calendar.time = game.gameDateTime
            String.format(
                "%d/%d",
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, mapOf())

    fun updateHomeIndex(index: Int) {
        homeIndexImp.value = index.coerceIn(0, 2)
    }

    fun updateScheduleIndex(index: Int) {
        scheduleIndexImp.value = index
    }

    private fun getDateStrings(): List<String> {
        val dateStrings = mutableListOf<String>()
        val calendar = NbaUtils.getCalendar()
        val currentTime = calendar.time
        repeat(SCHEDULE_DATE_RANGE) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            dateStrings.add(
                0,
                String.format(
                    "%d/%d",
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        calendar.time = currentTime
        dateStrings.add(
            String.format(
                "%d/%d",
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        repeat(SCHEDULE_DATE_RANGE) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            dateStrings.add(
                String.format(
                    "%d/%d",
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        return dateStrings
    }
}