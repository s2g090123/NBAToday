package com.jiachian.nbatoday.compose.screen.home

import android.text.format.DateUtils
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

data class StandingLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign
)

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
            it.timeInMillis - DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE + 1),
            it.timeInMillis + DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE)
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

    // Standing
    val teamStats = repository.getTeamStats().map {
        it.sortedByDescending { stats ->
            stats.winPercentage
        }.groupBy { stats ->
            stats.teamConference
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, mapOf())
    private val isRefreshingTeamStatsImp = MutableStateFlow(false)
    val isRefreshingTeamStats = isRefreshingTeamStatsImp.asStateFlow()
    private val standingIndexImp = MutableStateFlow(0)
    val standingIndex = standingIndexImp.asStateFlow()
    val standingLabel = derivedStateOf {
        listOf(
            StandingLabel(40.dp, "GP", TextAlign.End),
            StandingLabel(40.dp, "W", TextAlign.End),
            StandingLabel(40.dp, "L", TextAlign.End),
            StandingLabel(64.dp, "WIN%", TextAlign.End),
            StandingLabel(64.dp, "PTS", TextAlign.End),
            StandingLabel(64.dp, "FGM", TextAlign.End),
            StandingLabel(64.dp, "FGA", TextAlign.End),
            StandingLabel(64.dp, "FG%", TextAlign.End),
            StandingLabel(64.dp, "3PM", TextAlign.End),
            StandingLabel(64.dp, "3PA", TextAlign.End),
            StandingLabel(64.dp, "3P%", TextAlign.End),
            StandingLabel(64.dp, "FTM", TextAlign.End),
            StandingLabel(64.dp, "FTA", TextAlign.End),
            StandingLabel(64.dp, "FT%", TextAlign.End),
            StandingLabel(48.dp, "OREB", TextAlign.End),
            StandingLabel(48.dp, "DREB", TextAlign.End),
            StandingLabel(48.dp, "REB", TextAlign.End),
            StandingLabel(48.dp, "AST", TextAlign.End),
            StandingLabel(48.dp, "TOV", TextAlign.End),
            StandingLabel(48.dp, "STL", TextAlign.End),
            StandingLabel(48.dp, "BLK", TextAlign.End),
            StandingLabel(48.dp, "PF", TextAlign.End)
        )
    }

    init {
        updateTeamStats()
    }

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
        if (isRefreshingSchedule.value) return
        val cal = NbaUtils.getCalendar()
        cal.add(Calendar.DAY_OF_MONTH, -1)
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

    fun openGameBoxScore(game: NbaGame) {
        openScreen(NbaState.BoxScore(BoxScoreViewModel(game, repository, coroutineScope)))
    }

    fun updateTeamStats() {
        if (isRefreshingTeamStats.value) return
        coroutineScope.launch {
            isRefreshingTeamStatsImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshTeamStats()
            }
            isRefreshingTeamStatsImp.value = false
        }
    }

    fun updateStandingIndex(index: Int) {
        standingIndexImp.value = index.coerceIn(0, 1)
    }
}