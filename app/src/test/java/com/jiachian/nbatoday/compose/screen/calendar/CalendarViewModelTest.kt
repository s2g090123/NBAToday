package com.jiachian.nbatoday.compose.screen.calendar

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.launchAndCollect
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    private lateinit var viewModel: CalendarViewModel
    private val repository = TestRepository()
    private var currentState: NbaState? = null
    private val coroutineEnvironment = TestCoroutineEnvironment()

    @get:Rule
    val calendarRule = CalendarRule()

    @Before
    fun setup() = runTest {
        repository.refreshSchedule()
        viewModel = createViewModel(coroutineEnvironment.testDispatcherProvider)
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun calendar_getCurrentDateString() {
        viewModel.currentDateString.launchAndCollect(coroutineEnvironment)
        val calendar = DateUtils.getCalendar()
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
        viewModel.hasNextMonth.launchAndCollect(coroutineEnvironment)
        assertThat(viewModel.hasNextMonth.value, `is`(false))
    }

    @Test
    fun calendar_getHasPreviousMonth_returnsFalse() {
        viewModel.hasNextMonth.launchAndCollect(coroutineEnvironment)
        assertThat(viewModel.hasPreviousMonth.value, `is`(false))
    }

    @Test
    fun calendar_getCalendarData() {
        val expected = generateCalendarData()
        viewModel.calendarData.launchAndCollect(coroutineEnvironment)
        assertThat(viewModel.calendarData.value, `is`(expected))
    }

    @Test
    fun calendar_getGamesData() = coroutineEnvironment.testScope.runTest {
        viewModel.gamesData.launchAndCollect(coroutineEnvironment)
        val expected = generateGamesAndBets()
        assertThat(viewModel.gamesData.value, `is`(expected))
    }

    @Test
    fun getSelectDateData() {
        viewModel.selectDateData.launchAndCollect(coroutineEnvironment)
        val date = Date(BasicTime)
        val expected = generateCalendarData().firstOrNull {
            it.date == date
        }
        assertThat(viewModel.selectDateData.value, `is`(expected))
    }

    @Test
    fun calendar_getSelectGames() = coroutineEnvironment.testScope.runTest {
        viewModel.selectGames.launchAndCollect(coroutineEnvironment)
        val date = Date(BasicTime)
        val expected = generateGamesAndBets()
            .flatten()
            .filter { it.game.gameDate == date }
        assertThat(viewModel.selectGames.value, `is`(expected))
    }

    @Test
    fun calendar_selectDate_expectsCurrentDate() {
        viewModel.selectDateData.launchAndCollect(coroutineEnvironment)
        val date = Date(BasicTime)
        val calendarData = generateCalendarData()
        val expected = calendarData.firstOrNull { it.date == date }
        viewModel.selectDate(date)
        assertThat(viewModel.selectDateData.value, `is`(expected))
    }

    @Test
    fun calendar_nextMonth_expectsCurrentDate() {
        viewModel.currentDateString.launchAndCollect(coroutineEnvironment)
        viewModel.nextMonth()
        val expected = "Jan  2023"
        assertThat(viewModel.currentDateString.value.second, `is`(expected))
    }

    @Test
    fun calendar_previousMonth_expectsCurrentDate() {
        viewModel.currentDateString.launchAndCollect(coroutineEnvironment)
        viewModel.nextMonth()
        val expected = "Jan  2023"
        assertThat(viewModel.currentDateString.value.second, `is`(expected))
    }

    @Test
    fun calendar_openTeamStats_currentStateIsTeam() {
        viewModel.openTeamStats(HomeTeam)
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun calendar_openGameBoxScore_currentStateIsBoxScore() =
        coroutineEnvironment.testScope.runTest {
            val game = repository.getGamesAt(BasicTime).first()
            viewModel.openGameBoxScore(game)
            assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
        }

    @Test
    fun calendar_login_userLogin() = coroutineEnvironment.testScope.runTest {
        viewModel.login(UserAccount, UserPassword)
        advanceUntilIdle()
        val user = repository.user.value
        assertThat(user?.account, `is`(UserAccount))
        assertThat(user?.password, `is`(UserPassword))
    }

    @Test
    fun calendar_register_userRegister() {
        viewModel.register(UserAccount, UserPassword)
        val user = repository.user.value
        assertThat(user?.account, `is`(UserAccount))
        assertThat(user?.password, `is`(UserPassword))
    }

    @Test
    fun calendar_bet_returnsBetAndGame() = coroutineEnvironment.testScope.runTest {
        viewModel.login(UserAccount, UserPassword)
        val game = repository.getGamesAt(BasicTime).first()
        viewModel.bet(game.gameId, BasicNumber.toLong(), BasicNumber.toLong())
        val result = repository.getBetsAndGames(UserAccount).firstOrNull()?.firstOrNull()
        assertThat(result?.game, `is`(game))
        assertThat(result?.bet?.gameId, `is`(game.gameId))
        assertThat(result?.bet?.homePoints, `is`(BasicNumber.toLong()))
        assertThat(result?.bet?.awayPoints, `is`(BasicNumber.toLong()))
    }

    @Test
    fun calendar_getDate_returnsCurrentDate() {
        assertThat(viewModel.date, `is`(Date(BasicTime)))
    }

    private fun createViewModel(dispatcherProvider: DispatcherProvider): CalendarViewModel {
        return CalendarViewModel(
            date = Date(BasicTime),
            repository = repository,
            openScreen = {
                currentState = it
            },
            dispatcherProvider = dispatcherProvider
        )
    }

    private fun generateCalendarData(): List<CalendarData> {
        val data = mutableListOf<CalendarData>()
        val cal = DateUtils.getCalendar()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
            repeat(7) {
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
        return data
    }

    private suspend fun generateGamesAndBets(): List<List<GameAndBet>> {
        val games = repository.getGamesAndBets().first()
        val cal = DateUtils.getCalendar()
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
        val data = mutableListOf<List<GameAndBet>>()
        while (cal.get(Calendar.YEAR) <= year && cal.get(Calendar.MONTH) + 1 <= month) {
            repeat(7) {
                data.add(games.filter { it.game.gameDate.time == cal.timeInMillis })
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return data
    }
}
