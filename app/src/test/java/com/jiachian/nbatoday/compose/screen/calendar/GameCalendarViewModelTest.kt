package com.jiachian.nbatoday.compose.screen.calendar

import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameCalendarViewModelTest {

    private lateinit var viewModel: GameCalendarViewModel
    private val repository = TestRepository()
    private var currentState: NbaState? = null
    private val coroutineEnvironment = TestCoroutineEnvironment()

    @get:Rule
    val calendarRule = CalendarRule()

    @Before
    fun setup() = runTest {
        repository.refreshSchedule()
        viewModel = createViewModel(
            coroutineEnvironment.testScope,
            coroutineEnvironment.testDispatcherProvider
        )
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun calendar_getCurrentDateString() {
        val calendar = NbaUtils.getCalendar()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val expectedKey = year * 100 + month
        val expectedResult = "Jan  $year"
        val result = viewModel.currentDateString.value
        assertThat(result.second, `is`(expectedResult))
        assertThat(result.first, `is`(expectedKey))
    }

    @Test
    fun calendar_getHasNextMonth_returnsFalse() {
        assertThat(viewModel.hasNextMonth.value, `is`(false))
    }

    @Test
    fun calendar_getHasPreviousMonth_returnsFalse() {
        assertThat(viewModel.hasPreviousMonth.value, `is`(false))
    }

    @Test
    fun calendar_getCalendarData() {
        val expected = generateCalendarData()
        assertThat(viewModel.calendarData.value, `is`(expected))
    }

    @Test
    fun calendar_getGamesData() = runTest {
        val expected = generateGamesAndBets()
        assertThat(viewModel.gamesData.value, `is`(expected))
    }

    @Test
    fun getSelectDateData() = runTest {
        val date = Date(BASIC_TIME)
        val expected = generateCalendarData().firstOrNull {
            it.date == date
        }
        assertThat(viewModel.selectDateData.value, `is`(expected))
    }

    @Test
    fun calendar_getSelectGames() = runTest {
        val date = Date(BASIC_TIME)
        val expected = generateGamesAndBets()
            .flatten()
            .filter { it.game.gameDate == date }
        assertThat(viewModel.selectGames.value, `is`(expected))
    }

    @Test
    fun calendar_selectDate_expectsCurrentDate() {
        val date = Date(BASIC_TIME)
        val calendarData = generateCalendarData()
        val expected = calendarData.firstOrNull { it.date == date }
        viewModel.selectDate(date)
        assertThat(viewModel.selectDateData.value, `is`(expected))
    }

    @Test
    fun calendar_nextMonth_expectsCurrentDate() {
        viewModel.nextMonth()
        val expected = "Jan  2023"
        assertThat(viewModel.currentDateString.value.second, `is`(expected))
    }

    @Test
    fun calendar_previousMonth_expectsCurrentDate() {
        viewModel.nextMonth()
        val expected = "Jan  2023"
        assertThat(viewModel.currentDateString.value.second, `is`(expected))
    }

    @Test
    fun calendar_openTeamStats_currentStateIsTeam() {
        viewModel.openTeamStats(HOME_TEAM_ID)
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun calendar_openGameBoxScore_currentStateIsBoxScore() = runTest {
        val game = repository.getGamesAt(BASIC_TIME).first()
        viewModel.openGameBoxScore(game)
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun calendar_login_userLogin() {
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        val user = repository.user.value
        assertThat(user?.account, `is`(USER_ACCOUNT))
        assertThat(user?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun calendar_register_userRegister() {
        viewModel.register(USER_ACCOUNT, USER_PASSWORD)
        val user = repository.user.value
        assertThat(user?.account, `is`(USER_ACCOUNT))
        assertThat(user?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun calendar_bet_returnsBetAndGame() = runTest {
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        val game = repository.getGamesAt(BASIC_TIME).first()
        viewModel.bet(game.gameId, BASIC_NUMBER.toLong(), BASIC_NUMBER.toLong())
        val result = repository.getBetsAndGames(USER_ACCOUNT).firstOrNull()?.firstOrNull()
        assertThat(result?.game, `is`(game))
        assertThat(result?.bets?.gameId, `is`(game.gameId))
        assertThat(result?.bets?.homePoints, `is`(BASIC_NUMBER.toLong()))
        assertThat(result?.bets?.awayPoints, `is`(BASIC_NUMBER.toLong()))
    }

    @Test
    fun calendar_getDate_returnsCurrentDate() {
        assertThat(viewModel.date, `is`(Date(BASIC_TIME)))
    }

    private fun createViewModel(
        coroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider
    ): GameCalendarViewModel {
        return GameCalendarViewModel(
            date = Date(BASIC_TIME),
            repository = repository,
            openScreen = {
                currentState = it
            },
            coroutineScope = coroutineScope,
            dispatcherProvider = dispatcherProvider
        )
    }

    private fun generateCalendarData(): List<DateData> {
        val data = mutableListOf<DateData>()
        val cal = NbaUtils.getCalendar()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
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
        return data
    }

    private suspend fun generateGamesAndBets(): List<List<NbaGameAndBet>> {
        val games = repository.getGamesAndBets().first()
        val cal = NbaUtils.getCalendar()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        cal.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DATE, -(get(Calendar.DAY_OF_WEEK) - 1))
        }
        val data = mutableListOf<List<NbaGameAndBet>>()
        while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
            repeat(7) {
                data.add(games.filter { it.game.gameDate.time == cal.timeInMillis })
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return data
    }
}