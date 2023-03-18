package com.jiachian.nbatoday.compose.screen.home

import android.text.format.DateUtils
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.CelticsColors
import com.jiachian.nbatoday.data.TestDataStore
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamCeltics
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private var currentState: NbaState? = null
    private val repository = TestRepository()
    private val dataStore = TestDataStore()
    private val coroutineEnvironment = TestCoroutineEnvironment()
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val calendarRule = CalendarRule()

    @Before
    fun setup() = runTest {
        viewModel = createViewModel()
        repository.refreshSchedule()
    }

    @After
    fun teardown() {
        repository.clear()
        dataStore.clear()
        currentState = null
    }

    @Test
    fun home_getUser_afterLogin() {
        assertThat(viewModel.user.value, nullValue())
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun home_getHomeIndex_returnsZero() {
        assertThat(viewModel.homePage.value, `is`(HomePage.SCHEDULE))
    }

    @Test
    fun home_getScheduleDates() {
        val expected = generateDateData()
        assertThat(viewModel.scheduleDates, `is`(expected))
    }

    @Test
    fun home_getScheduleIndex_atCenter() {
        val dateData = generateDateData()
        val expect = dateData.size / 2
        assertThat(viewModel.scheduleIndex.value, `is`(expect))
    }

    @Test
    fun home_getScheduleGames() = runTest {
        val games = generateScheduleGames()
        assertThat(viewModel.scheduleGames.value, `is`(games))
    }

    @Test
    fun home_getStandingSort() {
        assertThat(viewModel.standingSort.value, `is`(StandingSort.WINP))
    }

    @Test
    fun home_getTeamStats() = runTest {
        val teams = repository.getTeamStats().first()
        val expected = teams.groupBy { it.teamConference }
        assertThat(viewModel.teamStats.value, `is`(expected))
    }

    @Test
    fun home_getStandingIndex_expectsZero() {
        assertThat(viewModel.selectConference.value, `is`(DefaultTeam.Conference.EAST))
    }

    @Test
    fun home_getStandingLabel() {
        val expected = generateStandingLabels()
        assertThat(viewModel.standingLabel.value, `is`(expected))
    }

    @Test
    fun home_switchToSchedule_expectsSchedule() {
        viewModel.updateHomePage(HomePage.SCHEDULE)
        assertThat(viewModel.homePage.value, `is`(HomePage.SCHEDULE))
    }

    @Test
    fun home_switchToStanding_expectsStanding() {
        viewModel.updateHomePage(HomePage.STANDING)
        assertThat(viewModel.homePage.value, `is`(HomePage.STANDING))
    }

    @Test
    fun home_switchToUser_expectsUser() {
        viewModel.updateHomePage(HomePage.USER)
        assertThat(viewModel.homePage.value, `is`(HomePage.USER))
    }

    @Test
    fun home_switchScheduleFirst_expectsFirst() {
        viewModel.updateScheduleIndex(0)
        assertThat(viewModel.scheduleIndex.value, `is`(0))
    }

    @Test
    fun home_switchScheduleLast_expectsLast() {
        val dateData = generateDateData()
        viewModel.updateScheduleIndex(dateData.size - 1)
        val expected = dateData.size - 1
        assertThat(viewModel.scheduleIndex.value, `is`(expected))
    }

    @Test
    fun home_updateTodaySchedule_expectsGames() = runTest {
        viewModel.updateTodaySchedule()
        val expected = repository.getGamesAndBets().first()
            .filter { it.game.gameDate == Date(BASIC_TIME) }
        val dateData = viewModel.scheduleDates[viewModel.scheduleIndex.value]
        assertThat(viewModel.scheduleGames.value[dateData], `is`(expected))
    }

    @Test
    fun home_openGameBoxScore_currentStateBoxScore() = runTest {
        val game = repository.getGamesAt(BASIC_TIME).first()
        viewModel.openGameBoxScore(game)
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun home_updateTeamStats_expectsTeam() = runTest {
        viewModel.updateTeamStats()
        val teams = repository.getTeamStats().first()
        val expected = teams.groupBy { it.teamConference }
        assertThat(viewModel.teamStats.value, `is`(expected))
    }

    @Test
    fun home_updateStandingConference_selectsEast() {
        viewModel.updateStandingConference(DefaultTeam.Conference.EAST)
        assertThat(viewModel.selectConference.value, `is`(DefaultTeam.Conference.EAST))
    }

    @Test
    fun home_updateStandingConference_selectsWest() {
        viewModel.updateStandingConference(DefaultTeam.Conference.WEST)
        assertThat(viewModel.selectConference.value, `is`(DefaultTeam.Conference.WEST))
    }

    @Test
    fun home_updateStandingSort_selectsGP() {
        val label = StandingLabel(
            BASIC_NUMBER.dp,
            BASIC_MINUTES,
            TextAlign.Center,
            StandingSort.GP
        )
        viewModel.updateStandingSort(label)
        assertThat(viewModel.standingSort.value, `is`(StandingSort.GP))
    }

    @Test
    fun home_openTeamStats_currentStateTeam() {
        viewModel.openTeamStats(HOME_TEAM_ID)
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun home_updateTheme_expectsCelticsColors() {
        viewModel.updateTheme(TeamCeltics().teamId, CelticsColors)
        assertThat(dataStore.themeColors.value, `is`(CelticsColors))
    }

    @Test
    fun home_openCalendar_currentStateCalendar() {
        val dateData = generateDateData().first()
        viewModel.openCalendar(dateData)
        assertThat(currentState, instanceOf(NbaState.Calendar::class.java))
    }

    @Test
    fun home_login_expectsUserLogin() {
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(repository.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(repository.user.value?.password, `is`(USER_PASSWORD))
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun home_logout_expectsUserLogout() {
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(repository.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(repository.user.value?.password, `is`(USER_PASSWORD))
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
        viewModel.logout()
        assertThat(repository.user.value?.account, nullValue())
        assertThat(repository.user.value?.password, nullValue())
        assertThat(viewModel.user.value?.account, nullValue())
        assertThat(viewModel.user.value?.password, nullValue())
    }

    @Test
    fun home_register_expectsUserRegister() {
        viewModel.register(USER_ACCOUNT, USER_PASSWORD)
        assertThat(repository.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(repository.user.value?.password, `is`(USER_PASSWORD))
        assertThat(viewModel.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(viewModel.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun home_bet_containsTargetGame() = runTest {
        val game = repository.getGamesAt(BASIC_TIME).first()
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        viewModel.bet(game.gameId, BASIC_NUMBER.toLong(), BASIC_NUMBER.toLong())
        val betsAndGames = repository.getBetsAndGames().first()
        assertThat(betsAndGames.find { it.game.gameId == game.gameId }, notNullValue())
    }

    @Test
    fun home_openBetScreen_currentStateBet() {
        viewModel.login(USER_ACCOUNT, USER_PASSWORD)
        viewModel.openBetScreen()
        assertThat(currentState, instanceOf(NbaState.Bet::class.java))
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            repository = repository,
            dataStore = dataStore,
            openScreen = {
                currentState = it
            },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider,
            coroutineScope = coroutineEnvironment.testScope
        )
    }

    private fun generateDateData(): List<DateData> {
        val cal = NbaUtils.getCalendar()
        cal.add(Calendar.DAY_OF_MONTH, -SCHEDULE_DATE_RANGE)
        val data = mutableListOf<DateData>()
        repeat(SCHEDULE_DATE_RANGE * 2 + 1) {
            data.add(
                DateData(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            )
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return data
    }

    private suspend fun generateScheduleGames(): Map<DateData, List<NbaGameAndBet>> {
        val cal = NbaUtils.getCalendar()
        val games = repository.getGamesAndBetsDuring(
            cal.timeInMillis - DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE + 1),
            cal.timeInMillis + DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE)
        ).first()
        return games.groupBy { game ->
            cal.time = game.game.gameDateTime
            DateData(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    private fun generateStandingLabels(): List<StandingLabel> {
        return listOf(
            StandingLabel(40.dp, "GP", TextAlign.End, StandingSort.GP),
            StandingLabel(40.dp, "W", TextAlign.End, StandingSort.W),
            StandingLabel(40.dp, "L", TextAlign.End, StandingSort.L),
            StandingLabel(64.dp, "WIN%", TextAlign.End, StandingSort.WINP),
            StandingLabel(64.dp, "PTS", TextAlign.End, StandingSort.PTS),
            StandingLabel(64.dp, "FGM", TextAlign.End, StandingSort.FGM),
            StandingLabel(64.dp, "FGA", TextAlign.End, StandingSort.FGA),
            StandingLabel(64.dp, "FG%", TextAlign.End, StandingSort.FGP),
            StandingLabel(64.dp, "3PM", TextAlign.End, StandingSort.PM3),
            StandingLabel(64.dp, "3PA", TextAlign.End, StandingSort.PA3),
            StandingLabel(64.dp, "3P%", TextAlign.End, StandingSort.PP3),
            StandingLabel(64.dp, "FTM", TextAlign.End, StandingSort.FTM),
            StandingLabel(64.dp, "FTA", TextAlign.End, StandingSort.FTA),
            StandingLabel(64.dp, "FT%", TextAlign.End, StandingSort.FTP),
            StandingLabel(48.dp, "OREB", TextAlign.End, StandingSort.OREB),
            StandingLabel(48.dp, "DREB", TextAlign.End, StandingSort.DREB),
            StandingLabel(48.dp, "REB", TextAlign.End, StandingSort.REB),
            StandingLabel(48.dp, "AST", TextAlign.End, StandingSort.AST),
            StandingLabel(48.dp, "TOV", TextAlign.End, StandingSort.TOV),
            StandingLabel(48.dp, "STL", TextAlign.End, StandingSort.STL),
            StandingLabel(48.dp, "BLK", TextAlign.End, StandingSort.BLK),
            StandingLabel(48.dp, "PF", TextAlign.End, StandingSort.PF)
        )
    }
}