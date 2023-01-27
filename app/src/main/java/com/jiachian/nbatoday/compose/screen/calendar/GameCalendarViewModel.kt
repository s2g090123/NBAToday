package com.jiachian.nbatoday.compose.screen.calendar

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

data class DateData(
    val date: Date,
    val month: Int,
    val day: Int,
    val isCurrentMonth: Boolean
)

class GameCalendarViewModel(
    val date: Date,
    private val repository: BaseRepository,
    private val openScreen: (state: NbaState) -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
) : ComposeViewModel() {

    private val games = repository.games

    private val currentYear: MutableStateFlow<Int>
    private val currentMonth: MutableStateFlow<Int>
    private val currentDay: MutableStateFlow<Int>

    val currentDate: Date
        get() {
            val cal = NbaUtils.getCalendar()
            cal.set(Calendar.YEAR, currentYear.value)
            cal.set(Calendar.MONTH, currentMonth.value - 1)
            cal.set(Calendar.DAY_OF_MONTH, currentDay.value)
            return cal.time
        }

    init {
        NbaUtils.getCalendar().apply {
            time = date
            currentYear = MutableStateFlow(get(Calendar.YEAR))
            currentMonth = MutableStateFlow(get(Calendar.MONTH) + 1)
            currentDay = MutableStateFlow(get(Calendar.DAY_OF_MONTH))
        }
    }

    private val lastDate = games.map {
        val cal = NbaUtils.getCalendar()
        it.lastOrNull()?.gameDateTime ?: cal.time
    }.stateIn(coroutineScope, SharingStarted.Eagerly, NbaUtils.getCalendar().time)
    private val firstDate = games.map {
        val cal = NbaUtils.getCalendar()
        it.firstOrNull()?.gameDate ?: cal.time
    }.stateIn(coroutineScope, SharingStarted.Eagerly, NbaUtils.getCalendar().time)

    val currentDateString = combine(
        currentYear, currentMonth
    ) { year, month ->
        (year * 100 + month) to (when (month) {
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
        } + "  " + year)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0 to "")

    val hasNextMonth = combine(
        currentYear, currentMonth, lastDate
    ) { _, _, _ ->
        hasNextMonth()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)
    val hasPreviousMonth = combine(
        currentYear, currentMonth, firstDate
    ) { _, _, _ ->
        hasPreviousMonth()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)

    private val calendarMap = mutableMapOf<String, List<DateData>>()
    private val gamesMap = mutableMapOf<String, List<List<NbaGame>>>()
    val calendarData = combine(
        currentYear, currentMonth
    ) { year, month ->
        getCalendar(year, month)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())
    val gamesData = combine(
        games, currentYear, currentMonth
    ) { games, year, month ->
        getGames(games, year, month)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())
    private val isLoadingGamesImp = MutableStateFlow(false)
    val isLoadingGames = isLoadingGamesImp.asStateFlow()

    private val selectDate = MutableStateFlow(date)
    val selectDateData = combine(
        selectDate,
        calendarData
    ) { date, data ->
        withContext(Dispatchers.IO) {
            data.firstOrNull { it.date == date }
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
    val selectGames = combine(
        selectDate,
        gamesData
    ) { date, data ->
        withContext(Dispatchers.IO) {
            data.firstOrNull { it.firstOrNull()?.gameDate == date }
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    fun selectDate(date: Date) {
        selectDate.value = date
    }

    fun nextMonth() {
        if (hasNextMonth()) {
            val cal = NbaUtils.getCalendar()
            cal.set(Calendar.YEAR, currentYear.value)
            cal.set(Calendar.MONTH, currentMonth.value - 1)
            cal.add(Calendar.MONTH, 1)
            currentYear.value = cal.get(Calendar.YEAR)
            currentMonth.value = cal.get(Calendar.MONTH) + 1
        }
    }

    fun previousMonth() {
        if (hasPreviousMonth()) {
            val cal = NbaUtils.getCalendar()
            cal.set(Calendar.YEAR, currentYear.value)
            cal.set(Calendar.MONTH, currentMonth.value - 1)
            cal.add(Calendar.MONTH, -1)
            currentYear.value = cal.get(Calendar.YEAR)
            currentMonth.value = cal.get(Calendar.MONTH) + 1
        }
    }

    private suspend fun getCalendar(year: Int, month: Int): List<DateData> {
        return withContext(Dispatchers.IO) {
            val key = "$year-$month"
            if (calendarMap.containsKey(key)) return@withContext calendarMap[key] ?: emptyList()
            val cal = NbaUtils.getCalendar().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            }
            val data = mutableListOf<DateData>()
            while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
                repeat(7) {
                    val currentMonth = cal.get(Calendar.MONTH) + 1
                    val currentDay = cal.get(Calendar.DAY_OF_MONTH)
                    data.add(
                        DateData(
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

    private suspend fun getGames(games: List<NbaGame>, year: Int, month: Int): List<List<NbaGame>> {
        return withContext(Dispatchers.IO) {
            val key = "$year-$month"
            if (gamesMap.containsKey(key)) return@withContext gamesMap[key] ?: emptyList()
            isLoadingGamesImp.value = true
            val cal = NbaUtils.getCalendar().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
            }
            val data = mutableListOf<List<NbaGame>>()
            while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
                repeat(7) {
                    data.add(games.filter { it.gameDate.time == cal.timeInMillis })
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            gamesMap[key] = data
            isLoadingGamesImp.value = false
            data
        }
    }

    private fun hasNextMonth(): Boolean {
        val year = currentYear.value
        val month = currentMonth.value
        val cal = NbaUtils.getCalendar()
        cal.time = lastDate.value
        val lastYear = cal.get(Calendar.YEAR)
        val lastMonth = cal.get(Calendar.MONTH) + 1
        return year < lastYear || (year == lastYear && month < lastMonth)
    }

    private fun hasPreviousMonth(): Boolean {
        val year = currentYear.value
        val month = currentMonth.value
        val cal = NbaUtils.getCalendar()
        cal.time = firstDate.value
        val firstYear = cal.get(Calendar.YEAR)
        val firstMonth = cal.get(Calendar.MONTH) + 1
        return year > firstYear || (year == firstYear && month > firstMonth)
    }

    fun openTeamStats(teamId: Int) {
        openScreen(NbaState.Team(TeamViewModel(teamId, repository, openScreen, coroutineScope)))
    }

    fun openGameBoxScore(game: NbaGame) {
        openScreen(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = game,
                    repository = repository,
                    showPlayerCareer = { playerId ->
                        openScreen(
                            NbaState.Player(
                                PlayerInfoViewModel(playerId, repository, coroutineScope)
                            )
                        )
                    },
                    coroutineScope = coroutineScope
                )
            )
        )
    }
}