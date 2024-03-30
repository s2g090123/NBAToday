package com.jiachian.nbatoday.compose.screen.calendar

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.DaysPerWeek
import com.jiachian.nbatoday.compose.screen.calendar.event.CalendarUIEvent
import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarDatesState
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarGamesState
import com.jiachian.nbatoday.compose.screen.calendar.state.CalendarTopBarState
import com.jiachian.nbatoday.compose.screen.calendar.state.MutableCalendarDatesState
import com.jiachian.nbatoday.compose.screen.calendar.state.MutableCalendarGamesState
import com.jiachian.nbatoday.compose.screen.calendar.state.MutableCalendarTopBarState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.user.GetUser
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for handling business logic related to [CalendarScreen].
 *
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(
    savedStateHandle: SavedStateHandle,
    private val gameUseCase: GameUseCase,
    getUser: GetUser,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val dateTime: Long =
        savedStateHandle.get<String>(MainRoute.Calendar.param)?.toLongOrNull() ?: throw Exception("dateTime is null.")

    private val user = getUser()

    private val topBarStateImp = MutableCalendarTopBarState()
    val topBarState: CalendarTopBarState = topBarStateImp

    private val datesStateImp = MutableCalendarDatesState()
    val datesState: CalendarDatesState = datesStateImp

    private val gamesStateImp = MutableCalendarGamesState()
    val gamesState: CalendarGamesState = gamesStateImp

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
        viewModelScope.launch(dispatcherProvider.default) {
            val (firstDate, lastDate) = gameUseCase.getFirstLastGameDate()
            currentCalendar
                .collectLatest { cal ->
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
                    Snapshot.withMutableSnapshot {
                        topBarStateImp.let { state ->
                            state.index = index
                            state.dateString = dateString
                            state.hasPrevious = hasPrev
                            state.hasNext = hasNext
                        }
                    }
                }
        }
    }

    private fun collectDatesState() {
        viewModelScope.launch {
            currentCalendar
                .collectLatest { cal ->
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)
                    val key = "$year-$month"
                    val dates = calendarDatesMap.getOrPut(key) {
                        datesStateImp.loading = true
                        getDates(year, month)
                    }
                    Snapshot.withMutableSnapshot {
                        datesStateImp.let { state ->
                            state.calendarDates = dates
                            state.loading = false
                        }
                    }
                }
        }
        viewModelScope.launch {
            selectedDate.collect {
                datesStateImp.selectedDate = it
            }
        }
    }

    private fun collectGamesState() {
        viewModelScope.launch {
            currentCalendar
                .collectLatest { cal ->
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)
                    val visible = isInCalendar(year, month, selectedDate.value)
                    gamesStateImp.visible = visible
                }
        }
        viewModelScope.launch {
            selectedDate
                .onEach {
                    gamesStateImp.loading = true
                }
                .flowOn(dispatcherProvider.main)
                .flatMapLatest {
                    getGames(it)
                }
                .mapLatest {
                    it.toGameCardState(user)
                }
                .flowOn(dispatcherProvider.default)
                .collect {
                    Snapshot.withMutableSnapshot {
                        gamesStateImp.let { state ->
                            state.games = it
                            state.loading = false
                            state.visible = true
                        }
                    }
                }
        }
    }

    private suspend fun getDates(year: Int, month: Int): List<CalendarDate> = withContext(dispatcherProvider.default) {
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

    fun onEvent(event: CalendarUIEvent) {
        when (event) {
            CalendarUIEvent.NextMonth -> nextMonth()
            CalendarUIEvent.PrevMonth -> prevMonth()
            is CalendarUIEvent.SelectDate -> selectedDate.value = event.date
        }
    }

    /**
     * Moves to the next month in the calendar.
     */
    private fun nextMonth() {
        if (topBarState.hasNext) {
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
        if (topBarState.hasPrevious) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, -1)
            }
        }
    }

    private suspend fun isInCalendar(year: Int, month: Int, date: Date): Boolean = withContext(dispatcherProvider.default) {
        DateUtils.getCalendar().run {
            time = date
            add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            if (get(Calendar.YEAR) == year && get(Calendar.MONTH) == month) return@run true
            add(Calendar.DATE, DaysPerWeek - 1)
            get(Calendar.YEAR) == year && get(Calendar.MONTH) == month
        }
    }
}
