package com.jiachian.nbatoday.compose.screen.home.schedule

import android.annotation.SuppressLint
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchedulePageViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val gameRepository: GameRepository,
    private val navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    val scheduleDates: List<DateData> = getDateData()
    private val scheduleIndexImp = MutableStateFlow(scheduleDates.size / 2)
    val scheduleIndex = scheduleIndexImp.asStateFlow()
    private val scheduleGamesImp = DateUtils.getCalendar().let {
        it.set(Calendar.HOUR, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        gameRepository.getGamesAndBetsDuring(
            it.timeInMillis - android.text.format.DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
            it.timeInMillis + android.text.format.DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
        )
    }
    val scheduleGames = scheduleGamesImp.map {
        val calendar = DateUtils.getCalendar()
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
        val calendar = DateUtils.getCalendar()
        calendar.add(Calendar.DAY_OF_MONTH, -ScheduleDateRange)
        repeat(ScheduleDateRange * 2 + 1) {
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
                scheduleRepository.updateSchedule(dateData.year, dateData.month, dateData.day)
            }
            isRefreshingScheduleImp.value = false
        }
    }

    fun clickScheduleGame(game: GameAndBet) {
        if (!game.game.isGamePlayed) {
            openTeamStats(game.game.homeTeam.team)
        } else {
            openGameBoxScore(game.game)
        }
    }

    fun openGameBoxScore(game: Game) {
        navigationController.navigateToBoxScore(game.gameId)
    }

    fun openTeamStats(team: NBATeam) {
        navigationController.navigateToTeam(team.teamId)
    }

    @SuppressLint("SimpleDateFormat")
    fun openCalendar(dateData: DateData) {
        val format = SimpleDateFormat("yyyy/MM/dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        val date = format.parse(dateData.dateString) ?: return
        navigationController.navigateToCalendar(date.time)
    }

    fun createGameStatusCardViewModel(gameAndBet: GameAndBet): GameStatusCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBet = gameAndBet,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    override fun close() {
        coroutineScope.cancel()
    }
}
