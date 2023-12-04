package com.jiachian.nbatoday.compose.screen.calendar

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

private const val DaysPerWeek = 7

class CalendarViewModel(
    dateTime: Long,
    repository: GameRepository,
    private val navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    private val date = Date(dateTime)

    private val games = repository.getGamesAndBets()

    private val currentYear: MutableStateFlow<Int>
    private val currentMonth: MutableStateFlow<Int>
    private val currentDay: MutableStateFlow<Int>

    init {
        DateUtils.getCalendar().apply {
            time = date
            currentYear = MutableStateFlow(get(Calendar.YEAR))
            currentMonth = MutableStateFlow(get(Calendar.MONTH) + 1)
            currentDay = MutableStateFlow(get(Calendar.DAY_OF_MONTH))
        }
    }

    val isProgressing = repository.isLoading

    private val isLoadingGamesImp = MutableStateFlow(false)
    val isLoadingGames = isLoadingGamesImp.asStateFlow()

    private val lastDate = games.map {
        val cal = DateUtils.getCalendar()
        it.lastOrNull()?.game?.gameDateTime ?: cal.time
    }.stateIn(coroutineScope, SharingStarted.Lazily, DateUtils.getCalendar().time)
    private val firstDate = games.map {
        val cal = DateUtils.getCalendar()
        it.firstOrNull()?.game?.gameDate ?: cal.time
    }.stateIn(coroutineScope, SharingStarted.Lazily, DateUtils.getCalendar().time)

    val currentDateString = combine(
        currentYear, currentMonth
    ) { year, month ->
        (year * 100 + month) to (
            when (month) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                else -> "Dec"
            } + "  " + year
            )
    }.stateIn(coroutineScope, SharingStarted.Lazily, 0 to "")

    val hasNextMonth = combine(
        currentYear, currentMonth, lastDate
    ) { _, _, _ ->
        hasNextMonth()
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    val hasPreviousMonth = combine(
        currentYear, currentMonth, firstDate
    ) { _, _, _ ->
        hasPreviousMonth()
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)

    private val calendarMap = mutableMapOf<String, List<CalendarData>>()
    val calendarData = combine(
        currentYear, currentMonth
    ) { year, month ->
        getCalendar(year, month)
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    val gamesData = combine(
        games, currentYear, currentMonth
    ) { games, year, month ->
        getGames(games, year, month)
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    private val selectDate = MutableStateFlow(date)
    val selectDateData = combine(
        selectDate,
        calendarData
    ) { date, data ->
        data.firstOrNull { it.date == date }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val selectGames = combine(
        selectDate,
        gamesData
    ) { date, data ->
        data.flatten()
            .filter { it.game.gameDate == date }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    fun selectDate(date: Date) {
        selectDate.value = date
    }

    fun nextMonth() {
        if (hasNextMonth()) {
            val cal = DateUtils.getCalendar()
            cal.set(Calendar.YEAR, currentYear.value)
            cal.set(Calendar.MONTH, currentMonth.value - 1)
            cal.add(Calendar.MONTH, 1)
            currentYear.value = cal.get(Calendar.YEAR)
            currentMonth.value = cal.get(Calendar.MONTH) + 1
        }
    }

    fun previousMonth() {
        if (hasPreviousMonth()) {
            val cal = DateUtils.getCalendar()
            cal.set(Calendar.YEAR, currentYear.value)
            cal.set(Calendar.MONTH, currentMonth.value - 1)
            cal.add(Calendar.MONTH, -1)
            currentYear.value = cal.get(Calendar.YEAR)
            currentMonth.value = cal.get(Calendar.MONTH) + 1
        }
    }

    private suspend fun getCalendar(year: Int, month: Int): List<CalendarData> {
        return withContext(dispatcherProvider.default) {
            val key = "$year-$month"
            if (calendarMap.containsKey(key)) return@withContext calendarMap[key] ?: emptyList()
            val cal = DateUtils.getCalendar().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            }
            val data = mutableListOf<CalendarData>()
            while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
                repeat(DaysPerWeek) {
                    val currentMonth = cal.get(Calendar.MONTH) + 1
                    val currentDay = cal.get(Calendar.DAY_OF_MONTH)
                    data.add(
                        CalendarData(
                            cal.time,
                            currentMonth,
                            currentDay,
                            currentMonth == month
                        )
                    )
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            calendarMap[key] = data
            data
        }
    }

    private suspend fun getGames(
        games: List<GameAndBet>,
        year: Int,
        month: Int
    ): List<List<GameAndBet>> {
        return withContext(dispatcherProvider.io) {
            isLoadingGamesImp.value = true
            val cal = DateUtils.getCalendar().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            }
            val data = mutableListOf<List<GameAndBet>>()
            while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
                repeat(DaysPerWeek) {
                    data.add(games.filter { it.game.gameDate.time == cal.timeInMillis })
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            isLoadingGamesImp.value = false
            data
        }
    }

    private fun hasNextMonth(): Boolean {
        val year = currentYear.value
        val month = currentMonth.value
        val cal = DateUtils.getCalendar()
        cal.time = lastDate.value
        val lastYear = cal.get(Calendar.YEAR)
        val lastMonth = cal.get(Calendar.MONTH) + 1
        return year < lastYear || (year == lastYear && month < lastMonth)
    }

    private fun hasPreviousMonth(): Boolean {
        val year = currentYear.value
        val month = currentMonth.value
        val cal = DateUtils.getCalendar()
        cal.time = firstDate.value
        val firstYear = cal.get(Calendar.YEAR)
        val firstMonth = cal.get(Calendar.MONTH) + 1
        return year > firstYear || (year == firstYear && month > firstMonth)
    }

    fun openTeamStats(team: NBATeam) {
        navigationController.navigateToTeam(team.teamId)
    }

    fun openGameBoxScore(game: Game) {
        navigationController.navigateToBoxScore(game.gameId)
    }

    fun createGameStatusCardViewModel(gameAndBet: GameAndBet): GameStatusCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBet = gameAndBet,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    override fun close() {
        coroutineScope.cancel()
        navigationController.backScreen(Route.CALENDAR)
    }
}
