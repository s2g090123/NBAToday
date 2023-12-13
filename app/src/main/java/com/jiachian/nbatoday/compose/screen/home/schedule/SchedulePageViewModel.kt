package com.jiachian.nbatoday.compose.screen.home.schedule

import android.annotation.SuppressLint
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
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
    private val scheduleRepository: ScheduleRepository,
    private val gameRepository: GameRepository,
    private val navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) {
    val dateData = createDateData()

    private var selectedDate = dateData[dateData.size / 2]

    private val games = DateUtils.getCalendar().run {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.MILLISECOND, 0)
        gameRepository.getGamesAndBetsDuring(
            timeInMillis - android.text.format.DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
            timeInMillis + android.text.format.DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
        )
    }
    val groupedGames = games.map {
        getGroupedGames(it)
    }.stateIn(coroutineScope, SharingStarted.Lazily, mapOf())

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    private fun createDateData(): List<DateData> {
        val range = ScheduleDateRange * 2 + 1
        return DateUtils.getCalendar().run {
            add(Calendar.DAY_OF_MONTH, -ScheduleDateRange)
            List(range) {
                DateData(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                ).also {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }
    }

    private suspend fun getGroupedGames(games: List<GameAndBets>): Map<DateData, List<GameAndBets>> {
        return withContext(dispatcherProvider.io) {
            DateUtils.getCalendar().run {
                games.groupBy { game ->
                    time = game.game.gameDateTime
                    DateData(
                        get(Calendar.YEAR),
                        get(Calendar.MONTH) + 1,
                        get(Calendar.DAY_OF_MONTH)
                    )
                }
            }
        }
    }

    fun selectDate(dateData: DateData) {
        selectedDate = dateData
    }

    fun updateSelectedSchedule() {
        if (isRefreshing.value) return
        coroutineScope.launch(dispatcherProvider.io) {
            isRefreshingImp.value = true
            scheduleRepository.updateSchedule(
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
            )
            isRefreshingImp.value = false
        }
    }

    fun onClickGame(game: GameAndBets) {
        if (!game.game.isGamePlayed) {
            navigationController.navigateToTeam(game.game.homeTeam.team.teamId)
        } else {
            navigationController.navigateToBoxScore(game.game.gameId)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun onClickCalendar() {
        SimpleDateFormat("yyyy/MM/dd").let { format ->
            format.timeZone = TimeZone.getTimeZone("EST")
            format.parse(selectedDate.dateString)?.time
        }?.run {
            navigationController.navigateToCalendar(this)
        }
    }

    fun createGameCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return composeViewModelProvider.getGameCardViewModel(
            gameAndBets = gameAndBets,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }
}
