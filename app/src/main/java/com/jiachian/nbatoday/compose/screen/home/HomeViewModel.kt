package com.jiachian.nbatoday.compose.screen.home

import android.text.format.DateUtils
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel(
    private val repository: BaseRepository,
    private val openScreen: (state: NbaState) -> Unit
) : ComposeViewModel() {

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
    private val isRefreshingScheduleImp = MutableStateFlow(false)
    val isRefreshingSchedule = isRefreshingScheduleImp.asStateFlow()

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

    fun updateTodaySchedule() {
        val cal = NbaUtils.getCalendar()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        coroutineScope.launch {
            isRefreshingScheduleImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshSchedule(year, month, day)
            }
            isRefreshingScheduleImp.value = false
        }
    }

    fun openGameBoxScore(gameId: String) {
        openScreen(NbaState.BoxScore(BoxScoreViewModel(gameId, repository, coroutineScope)))
    }
}