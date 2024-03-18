package com.jiachian.nbatoday.compose.screen.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.DaysPerWeek
import com.jiachian.nbatoday.compose.screen.calendar.event.CalendarEvent
import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarDatesState
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarGamesState
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarTopBarState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.user.UserUseCase
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * ViewModel for handling business logic related to [CalendarScreen].
 *
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(
    savedStateHandle: SavedStateHandle,
    private val gameUseCase: GameUseCase,
    userUseCase: UserUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val dateTime: Long =
        savedStateHandle.get<String>(MainRoute.Calendar.param)?.toLongOrNull() ?: throw Exception("dateTime is null.")

    private val user = userUseCase.getUser()

    private val topBarStateImp = MutableStateFlow(CalendarTopBarState())
    val topBarState = topBarStateImp.asStateFlow()

    private val datesStateFlowImp = MutableStateFlow(CalendarDatesState())
    val datesStateFlow = datesStateFlowImp.asStateFlow()

    private val gamesStateImp = MutableStateFlow(CalendarGamesState())
    val gamesState = gamesStateImp.asStateFlow()

    private val currentCalendar = DateUtils.getCalendar().let {
        it.timeInMillis = dateTime
        MutableStateFlow(it)
    }

    private val selectedDate = MutableStateFlow(Date(dateTime))

    private val calendarDatesMap = mutableMapOf<String, List<CalendarDate>>()

    init {
        collectTopBarState()
        collectDatesState()
        collectGamesState()
    }

    private fun collectTopBarState() {
        viewModelScope.launch {
            val (firstDate, lastDate) = gameUseCase.getFirstLastGameDate()
            currentCalendar
                .mapLatest { cal ->
                    val (index, dateString) = cal.run {
                        val year = get(Calendar.YEAR)
                        val month = get(Calendar.MONTH)
                        year * 100 + month to DateUtils.getDateString(year, month)
                    }
                    val tmpCalendar = DateUtils.getCalendar()
                    val hasPrev = tmpCalendar.run {
                        time = firstDate
                        get(Calendar.YEAR) < cal.get(Calendar.YEAR) || get(Calendar.MONTH) < cal.get(Calendar.MONTH)
                    }
                    val hasNext = tmpCalendar.run {
                        time = lastDate
                        get(Calendar.YEAR) > cal.get(Calendar.YEAR) || get(Calendar.MONTH) > cal.get(Calendar.MONTH)
                    }
                    CalendarTopBarState(
                        index = index,
                        dateString = dateString,
                        hasPrevious = hasPrev,
                        hasNext = hasNext
                    )
                }
                .collect {
                    topBarStateImp.value = it
                }
        }
    }

    private fun collectDatesState() {
        viewModelScope.launch {
            currentCalendar
                .flatMapLatest { cal ->
                    flow {
                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH)
                        val key = "$year-$month"
                        val dates = calendarDatesMap.getOrPut(key) {
                            emit(datesStateFlow.value.copy(loading = true))
                            getDates(year, month)
                        }
                        emit(datesStateFlow.value.copy(calendarDates = dates, loading = false))
                    }.flowOn(dispatcherProvider.default)
                }
                .collect {
                    datesStateFlowImp.value = it
                }
        }
        viewModelScope.launch {
            selectedDate.collect {
                datesStateFlowImp.value = datesStateFlow.value.copy(selectedDate = it)
            }
        }
    }

    private fun collectGamesState() {
        viewModelScope.launch {
            currentCalendar
                .mapLatest { cal ->
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)
                    isInCalendar(year, month, selectedDate.value)
                }
                .collect {
                    gamesStateImp.value = gamesState.value.copy(visible = it)
                }
        }
        viewModelScope.launch {
            selectedDate
                .onEach {
                    gamesStateImp.value = gamesState.value.copy(loading = true)
                }
                .flatMapLatest {
                    getGames(it)
                }
                .map {
                    it.toGameCardState(user)
                }
                .flowOn(dispatcherProvider.default)
                .collect {
                    gamesStateImp.value = gamesState.value.copy(games = it, loading = false, visible = true)
                }
        }
    }

    private fun getDates(year: Int, month: Int): List<CalendarDate> {
        return DateUtils.getCalendar()
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
                calendarDates
            }
    }

    private fun getGames(date: Date): Flow<List<GameAndBets>> {
        return DateUtils
            .getCalendar()
            .run {
                time = date
                val after = timeInMillis
                add(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.MILLISECOND, -1)
                val before = timeInMillis
                after to before
            }
            .let { (from, to) ->
                gameUseCase.getGamesDuring(from, to)
            }
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            CalendarEvent.NextMonth -> nextMonth()
            CalendarEvent.PrevMonth -> prevMonth()
            is CalendarEvent.SelectDate -> selectedDate.value = event.date
        }
    }

    /**
     * Moves to the next month in the calendar.
     */
    private fun nextMonth() {
        if (topBarState.value.hasPrevious) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, 1)
            }
        }
    }

    /**
     * Moves to the previous month in the calendar.
     */
    private fun prevMonth() {
        if (topBarState.value.hasPrevious) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, -1)
            }
        }
    }

    private fun isInCalendar(year: Int, month: Int, date: Date): Boolean {
        return DateUtils.getCalendar().run {
            time = date
            add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            if (get(Calendar.YEAR) == year && get(Calendar.MONTH) == month) return@run true
            add(Calendar.DATE, DaysPerWeek - 1)
            get(Calendar.YEAR) == year && get(Calendar.MONTH) == month
        }
    }
}
