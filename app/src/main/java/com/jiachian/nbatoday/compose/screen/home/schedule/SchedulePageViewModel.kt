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

/**
 * ViewModel for handling business logic related to [SchedulePage].
 *
 * @property scheduleRepository The repository for interacting with [GameAndBets].
 * @property gameRepository The repository for interacting with [GameAndBets].
 * @property navigationController The controller for navigation within the app.
 * @property composeViewModelProvider The provider for creating ComposeViewModel instances.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class SchedulePageViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val gameRepository: GameRepository,
    private val navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main)
) {
    // the date data for the schedule page on the TabBar
    val dateData = createDateData()

    // selected date for the schedule page
    private var selectedDate = dateData[dateData.size / 2]

    // the list of games within the specified date range
    private val games = DateUtils.getCalendar().run {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.MILLISECOND, 0)
        gameRepository.getGamesAndBetsDuring(
            timeInMillis - DateUtils.DAY_IN_MILLIS * (ScheduleDateRange + 1),
            timeInMillis + DateUtils.DAY_IN_MILLIS * (ScheduleDateRange)
        )
    }

    // the grouped games based on their date
    val groupedGamesState = games
        .map { UIState.Loaded(getGroupedGames(it)) }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val refreshingImp = MutableStateFlow(false)
    val refreshing = refreshingImp.asStateFlow()

    private val gameCardViewModelMap = mutableMapOf<GameAndBets, GameCardViewModel>()

    /**
     * Creates a list of date data based on the specified date range.
     *
     * @return List of DateData instances.
     */
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

    /**
     * Groups the games based on their date.
     *
     * @param games List of GameAndBets instances.
     * @return Map with DateData as keys and lists of GameAndBets as values.
     */
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

    /**
     * Updates the selected schedule based on the chosen date.
     */
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

    /**
     * Handles click event for a game, navigating to the team or box score screen.
     *
     * @param game The selected GameAndBets instance.
     */
    fun onClickGame(game: GameAndBets) {
        if (!game.game.gamePlayed) {
            navigationController.navigateToTeam(game.game.homeTeam.team.teamId)
        } else {
            navigationController.navigateToBoxScore(game.game.gameId)
        }
    }

    /**
     * Handles click event for the calendar, navigating to the selected date on the calendar screen.
     */
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
