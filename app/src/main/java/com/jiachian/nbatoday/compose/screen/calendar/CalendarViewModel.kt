package com.jiachian.nbatoday.compose.screen.calendar

import com.jiachian.nbatoday.DaysPerWeek
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.calendar.CalendarDate
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel(
    dateTime: Long,
    private val repository: GameRepository,
    navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = Route.CALENDAR
) {
    private val currentCalendar: MutableStateFlow<Calendar>

    private val selectedDateImp = MutableStateFlow(Date(dateTime))
    val selectedDate = selectedDateImp.asStateFlow()

    private val selectedGamesImp = MutableStateFlow(emptyList<GameAndBets>())
    val selectedGames = selectedGamesImp.asStateFlow()

    init {
        DateUtils.getCalendar().apply {
            timeInMillis = dateTime
            currentCalendar = MutableStateFlow(this)
        }
        collectSelectedGames()
    }

    private val isLoadingGamesImp = MutableStateFlow(false)
    val isLoadingGames = isLoadingGamesImp.asStateFlow()

    private val isLoadingCalendarImp = MutableStateFlow(false)
    val isLoadingCalendar = isLoadingCalendarImp.asStateFlow()

    private val lastGameDate = repository.getLastGameDateTime()
        .stateIn(coroutineScope, SharingStarted.Eagerly, Date(dateTime))
    private val firstGameDate = repository.getFirstGameDateTime()
        .stateIn(coroutineScope, SharingStarted.Eagerly, Date(dateTime))

    val selectedGamesVisible = combine(
        currentCalendar,
        selectedDate
    ) { cal, selectedDate ->
        isInCalendar(cal, selectedDate)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)

    val numberAndDateString = currentCalendar.map { cal ->
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        year * 100 + month to DateUtils.getDateString(year, month)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0 to "")

    val hasNextMonth = combine(
        currentCalendar, lastGameDate
    ) { cal, lastDate ->
        DateUtils.getCalendar()
            .apply { time = lastDate }
            .run {
                get(Calendar.YEAR) > cal.get(Calendar.YEAR) || get(Calendar.MONTH) > cal.get(Calendar.MONTH)
            }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)
    val hasLastMonth = combine(
        currentCalendar, firstGameDate
    ) { cal, lastDate ->
        DateUtils.getCalendar()
            .apply { time = lastDate }
            .run {
                get(Calendar.YEAR) < cal.get(Calendar.YEAR) || get(Calendar.MONTH) < cal.get(Calendar.MONTH)
            }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)

    private val calendarDatesMap = mutableMapOf<String, List<CalendarDate>>()

    val calendarDates = currentCalendar.map { cal ->
        getCalendarDates(cal)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectSelectedGames() {
        coroutineScope.launch(dispatcherProvider.io) {
            selectedDate
                .onEach { isLoadingGamesImp.value = true }
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
                    isLoadingGamesImp.value = false
                }
        }
    }

    fun selectDate(date: Date) {
        selectedDateImp.value = date
    }

    fun nextMonth() {
        if (hasNextMonth.value) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, 1)
            }
        }
    }

    fun lastMonth() {
        if (hasLastMonth.value) {
            currentCalendar.value = DateUtils.getCalendar().apply {
                time = currentCalendar.value.time
                add(Calendar.MONTH, -1)
            }
        }
    }

    private suspend fun getCalendarDates(calendar: Calendar): List<CalendarDate> {
        return withContext(dispatcherProvider.io) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val key = "$year-$month"
            if (calendarDatesMap.containsKey(key)) return@withContext calendarDatesMap[key] ?: emptyList()
            isLoadingCalendarImp.value = true
            val dates = DateUtils.getCalendar()
                .apply {
                    set(year, month, 1, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                    add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
                }
                .run {
                    val calendarDates = mutableListOf<CalendarDate>()
                    while (true) {
                        val isLastYear = get(Calendar.YEAR) < year && get(Calendar.MONTH) > month
                        val isCurrentMonth = get(Calendar.YEAR) == year && get(Calendar.MONTH) <= month
                        if (!isLastYear && !isCurrentMonth) break
                        repeat(DaysPerWeek) {
                            calendarDates.add(
                                CalendarDate(
                                    date = time,
                                    day = get(Calendar.DAY_OF_MONTH),
                                    isCurrentMonth = get(Calendar.MONTH) == month
                                )
                            )
                            add(Calendar.DAY_OF_MONTH, 1)
                        }
                    }
                    calendarDates
                }
            calendarDatesMap[key] = dates
            isLoadingCalendarImp.value = false
            dates
        }
    }

    fun clickGameCard(game: Game) {
        if (game.isGamePlayed) {
            navigationController.navigateToBoxScore(game.gameId)
        } else {
            navigationController.navigateToTeam(game.homeTeamId)
        }
    }

    fun createGameCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return composeViewModelProvider.getGameCardViewModel(
            gameAndBets = gameAndBets,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
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
}
