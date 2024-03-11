package com.jiachian.nbatoday.compose.screen.calendar

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.DaysPerWeek
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.WhileSubscribed5000
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

/**
 * ViewModel for handling business logic related to [CalendarScreen].
 *
 * @property dateTime The initial date and time for the calendar.
 * @property repository The repository for interacting with [GameAndBets].
 * @property navigationController The controller for navigation within the app.
 * @property composeViewModelProvider The provider for creating ComposeViewModel instances.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class CalendarViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: GameRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val dateTime: Long =
        savedStateHandle.get<String>(MainRoute.Calendar.param)?.toLongOrNull() ?: throw Exception("dateTime is null.")

    private val currentCalendar: MutableStateFlow<Calendar> = DateUtils.getCalendar().let {
        it.timeInMillis = dateTime
        MutableStateFlow(it)
    }

    private val selectedDateImp = MutableStateFlow(Date(dateTime))
    val selectedDate = selectedDateImp.asStateFlow()

    // the list of games for the selected date
    private val selectedGamesImp = MutableStateFlow(emptyList<GameAndBets>())
    val selectedGames = selectedGamesImp.asStateFlow()

    // the loading status of [selectedGames]
    private val loadingGamesImp = MutableStateFlow(false)
    val loadingGames = loadingGamesImp.asStateFlow()

    // the loading status of [calendarDates]
    private val loadingCalendar = MutableStateFlow(false)

    private val lastGameDate = repository.getLastGameDateTime()
    private val firstGameDate = repository.getFirstGameDateTime()

    // Initialize the [currentCalendar] based on the provided date and time.
    init {
        collectSelectedGames()
    }

    val selectedGamesVisible = combine(
        currentCalendar,
        selectedDate
    ) { cal, selectedDate ->
        isInCalendar(cal, selectedDate)
    }.stateIn(viewModelScope, WhileSubscribed5000, false)

    val numberAndDateString = currentCalendar.map { cal ->
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        year * 100 + month to DateUtils.getDateString(year, month)
    }.stateIn(viewModelScope, WhileSubscribed5000, 0 to "")

    val hasNextMonth = combine(
        currentCalendar, lastGameDate
    ) { cal, lastDate ->
        DateUtils.getCalendar()
            .apply { time = lastDate }
            .run {
                get(Calendar.YEAR) > cal.get(Calendar.YEAR) || get(Calendar.MONTH) > cal.get(Calendar.MONTH)
            }
    }.stateIn(viewModelScope, WhileSubscribed5000, false)
    val hasLastMonth = combine(
        currentCalendar, firstGameDate
    ) { cal, lastDate ->
        DateUtils.getCalendar()
            .apply { time = lastDate }
            .run {
                get(Calendar.YEAR) < cal.get(Calendar.YEAR) || get(Calendar.MONTH) < cal.get(Calendar.MONTH)
            }
    }.stateIn(viewModelScope, WhileSubscribed5000, false)

    private val calendarDatesMap = mutableMapOf<String, List<CalendarDate>>()

    private val calendarDates = currentCalendar.map { cal ->
        getCalendarDates(cal)
    }.flowOn(dispatcherProvider.io)
    val calendarDatesState = combine(
        loadingCalendar,
        calendarDates
    ) { loading, dates ->
        if (loading) return@combine UIState.Loading()
        UIState.Loaded(dates)
    }.stateIn(viewModelScope, WhileSubscribed5000, UIState.Loading())

    private val gameCardViewModelMap = mutableMapOf<GameAndBets, GameCardViewModel>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectSelectedGames() {
        viewModelScope.launch(dispatcherProvider.io) {
            selectedDate
                .onEach { loadingGamesImp.value = true }
                .flatMapLatest { selectedDate ->
                    DateUtils
                        .getCalendar()
                        .run {
                            time = selectedDate
                            val after = timeInMillis
                            add(Calendar.DAY_OF_MONTH, 1)
                            add(Calendar.MILLISECOND, -1)
                            val before = timeInMillis
                            after to before
                        }
                        .let { (after, before) ->
                            repository.getGamesAndBetsDuring(after, before)
                        }
                }
                .collect { games ->
                    selectedGamesImp.value = games
                    loadingGamesImp.value = false
                }
        }
    }

    fun selectDate(date: Date) {
        selectedDateImp.value = date
    }

    /**
     * Moves to the next month in the calendar.
     */
    fun nextMonth() {
        if (hasNextMonth()) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, 1)
            }
        }
    }

    /**
     * Moves to the previous month in the calendar.
     */
    fun lastMonth() {
        if (hasLastMonth()) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, -1)
            }
        }
    }

    /**
     * Retrieves the list of dates for the current calendar month.
     *
     * @param calendar The calendar instance representing the current month.
     * @return List of CalendarDate objects representing each day in the month.
     */
    private fun getCalendarDates(calendar: Calendar): List<CalendarDate> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val key = "$year-$month"
        return calendarDatesMap.getOrPut(key) {
            loadingCalendar.value = true
            DateUtils.getCalendar()
                .apply {
                    set(year, month, 1, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
                }
                .run {
                    val calendarDates = mutableListOf<CalendarDate>()
                    while (true) {
                        val lastYear = get(Calendar.YEAR) < year && get(Calendar.MONTH) > month
                        val currentMonth = get(Calendar.YEAR) == year && get(Calendar.MONTH) <= month
                        if (!lastYear && !currentMonth) break
                        repeat(DaysPerWeek) {
                            calendarDates.add(
                                CalendarDate(
                                    date = time,
                                    day = get(Calendar.DAY_OF_MONTH),
                                    currentMonth = get(Calendar.MONTH) == month
                                )
                            )
                            add(Calendar.DAY_OF_MONTH, 1)
                        }
                    }
                    loadingCalendar.value = false
                    calendarDates
                }
        }
    }

    fun getGameCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return GameCardViewModel(
            gameAndBets = gameAndBets,
            betRepository = get(BetRepository::class.java),
            userRepository = get(UserRepository::class.java),
            dispatcherProvider = dispatcherProvider,
            coroutineScope = viewModelScope,
        )
    }

    private fun isInCalendar(calendar: Calendar, date: Date): Boolean {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        return DateUtils.getCalendar().run {
            time = date
            add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            if (get(Calendar.YEAR) == year && get(Calendar.MONTH) == month) return@run true
            add(Calendar.DATE, DaysPerWeek - 1)
            get(Calendar.YEAR) == year && get(Calendar.MONTH) == month
        }
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun hasNextMonth() = hasNextMonth.value

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun hasLastMonth() = hasLastMonth.value
}
