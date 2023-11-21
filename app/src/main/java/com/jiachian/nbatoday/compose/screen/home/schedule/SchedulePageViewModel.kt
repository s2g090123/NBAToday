package com.jiachian.nbatoday.compose.screen.home.schedule

import android.annotation.SuppressLint
import android.text.format.DateUtils
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.ScreenStateHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchedulePageViewModel(
    private val repository: BaseRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {

    val scheduleDates: List<DateData> = getDateData()
    private val scheduleIndexImp = MutableStateFlow(scheduleDates.size / 2)
    val scheduleIndex = scheduleIndexImp.asStateFlow()
    private val scheduleGamesImp = NbaUtils.getCalendar().let {
        it.set(Calendar.HOUR, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        repository.getGamesAndBetsDuring(
            it.timeInMillis - DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE + 1),
            it.timeInMillis + DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE)
        )
    }
    val scheduleGames = scheduleGamesImp.map {
        val calendar = NbaUtils.getCalendar()
        it.groupBy { game ->
            calendar.time = game.game.gameDateTime
            DateData(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, mapOf())
    private val isRefreshingScheduleImp = MutableStateFlow(false)
    val isRefreshingSchedule = isRefreshingScheduleImp.asStateFlow()

    fun updateScheduleIndex(index: Int) {
        if (index !in scheduleDates.indices) return
        scheduleIndexImp.value = index
    }

    private fun getDateData(): List<DateData> {
        val output = mutableListOf<DateData>()
        val calendar = NbaUtils.getCalendar()
        calendar.add(Calendar.DAY_OF_MONTH, -SCHEDULE_DATE_RANGE)
        repeat(SCHEDULE_DATE_RANGE * 2 + 1) {
            output.add(
                DateData(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return output
    }

    fun updateTodaySchedule() {
        if (isRefreshingSchedule.value) return
        val dateData = scheduleDates.getOrNull(scheduleIndex.value) ?: return
        coroutineScope.launch {
            isRefreshingScheduleImp.value = true
            withContext(dispatcherProvider.io) {
                repository.refreshSchedule(dateData.year, dateData.month, dateData.day)
            }
            isRefreshingScheduleImp.value = false
        }
    }

    fun clickScheduleGame(game: NbaGameAndBet) {
        if (!game.game.isGamePlayed) {
            openTeamStats(game.game.homeTeam.team)
        } else {
            openGameBoxScore(game.game)
        }
    }

    fun openGameBoxScore(game: NbaGame) {
        screenStateHelper.openScreen(NbaScreenState.BoxScore(game))
    }

    fun openTeamStats(team: NBATeam) {
        screenStateHelper.openScreen(NbaScreenState.Team(team))
    }

    @SuppressLint("SimpleDateFormat")
    fun openCalendar(dateData: DateData) {
        val format = SimpleDateFormat("yyyy/MM/dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        val date = format.parse(dateData.dateString) ?: return
        screenStateHelper.openScreen(NbaScreenState.Calendar(date))
    }

    fun createGameStatusCardViewModel(gameAndBet: NbaGameAndBet): GameStatusCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBet = gameAndBet,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }
}
