package com.jiachian.nbatoday.compose.screen.home.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.compose.screen.card.GameCardState
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.usecase.user.UserUseCase
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
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
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 */
class SchedulePageViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val gameRepository: GameRepository,
    userUseCase: UserUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val user = userUseCase.getUser()

    // the date data for the schedule page on the TabBar
    val dateData = createDateData()

    // selected date for the schedule page
    var selectedDate = dateData[dateData.size / 2]
        private set

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
        .flowOn(dispatcherProvider.default)
        .stateIn(viewModelScope, SharingStarted.Lazily, UIState.Loading())

    private val refreshingImp = MutableStateFlow(false)
    val refreshing = refreshingImp.asStateFlow()

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
    private fun getGroupedGames(games: List<GameAndBets>): Map<DateData, List<GameCardState>> {
        return DateUtils.getCalendar().run {
            games
                .toGameCardState(user)
                .groupBy { state ->
                    time = state.data.game.gameDateTime
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
        viewModelScope.launch(dispatcherProvider.io) {
            refreshingImp.value = true
            scheduleRepository.updateSchedule(
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
            )
            refreshingImp.value = false
        }
    }
}
