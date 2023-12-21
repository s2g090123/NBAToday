package com.jiachian.nbatoday.compose.screen.home.schedule

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.state.UIState
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
            timeInMillis - DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
            timeInMillis + DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
        )
    }

    val groupedGamesState = games
        .map { UIState.Loaded(getGroupedGames(it)) }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val refreshingImp = MutableStateFlow(false)
    val refreshing = refreshingImp.asStateFlow()

    private val gameCardViewModelMap = mutableMapOf<GameAndBets, GameCardViewModel>()

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

    private fun getGroupedGames(games: List<GameAndBets>): Map<DateData, List<GameAndBets>> {
        return DateUtils.getCalendar().run {
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

    fun selectDate(dateData: DateData) {
        selectedDate = dateData
    }

    fun updateSelectedSchedule() {
        if (refreshing.value) return
        coroutineScope.launch(dispatcherProvider.io) {
            refreshingImp.value = true
            scheduleRepository.updateSchedule(
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
            )
            refreshingImp.value = false
        }
    }

    fun onClickGame(game: GameAndBets) {
        if (!game.game.gamePlayed) {
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

    fun getGameCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return gameCardViewModelMap.getOrPut(gameAndBets) {
            composeViewModelProvider.getGameCardViewModel(
                gameAndBets = gameAndBets,
                dispatcherProvider = dispatcherProvider,
                coroutineScope = coroutineScope
            )
        }
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun getSelectedDate() = selectedDate
}
