package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.NbaGameFactory
import com.jiachian.nbatoday.data.PlayerStatsFactory
import com.jiachian.nbatoday.data.TeamStatsFactory
import com.jiachian.nbatoday.data.TestDataStore
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val repository = TestRepository()
    private val dataStore = TestDataStore()
    private val coroutineEnvironment = TestCoroutineEnvironment()

    @get:Rule
    val setMainDispatcherRule = SetMainDispatcherRule(coroutineEnvironment)

    @Before
    fun setup() {
        viewModel = MainViewModel(
            repositoryProvider = repository,
            dataStore = dataStore,
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
    }

    @After
    fun teardown() {
        repository.clear()
        dataStore.clear()
    }

    @Test
    fun main_checksStateStackIsInitState() {
        viewModel.stateStack.launchAndCollect(coroutineEnvironment)
        assertThat(viewModel.stateStack.value.size, `is`(1))
        assertThat(
            viewModel.stateStack.value.first(),
            CoreMatchers.instanceOf(NbaState.Home::class.java)
        )
    }

    @Test
    fun main_checksCurrentStateIsInitState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Home::class.java)
        )
    }

    @Test
    fun main_updateBoxScoreState_checksCurrentState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = NbaGameFactory.getPlayingGame(),
                    repository = repository,
                    showPlayerCareer = {}
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.BoxScore::class.java)
        )
    }

    @Test
    fun main_updateTeamState_checksCurrentState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.Team(
                TeamViewModel(
                    team = TeamStatsFactory.getHomeTeamStats().team,
                    teamRepository = repository,
                    openScreen = {}
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Team::class.java)
        )
    }

    @Test
    fun main_updatePlayerState_checksCurrentState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.Player(
                PlayerViewModel(
                    playerId = PlayerStatsFactory.getHomePlayerStats().playerId,
                    repository = repository
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Player::class.java)
        )
    }

    @Test
    fun main_updateCalendarState_checksCurrentState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.Calendar(
                CalendarViewModel(
                    date = Date(BASIC_TIME),
                    repository = repository,
                    openScreen = {}
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Calendar::class.java)
        )
    }

    @Test
    fun main_updateBetState_checksCurrentState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.Bet(
                BetViewModel(
                    account = USER_ACCOUNT,
                    repository = repository,
                    openScreen = {}
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Bet::class.java)
        )
    }

    @Test
    fun main_updateBoxScoreStateAndBack_backToHomeState() {
        viewModel.currentState.launchAndCollect(coroutineEnvironment)
        viewModel.updateState(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = NbaGameFactory.getPlayingGame(),
                    repository = repository,
                    showPlayerCareer = {}
                )
            )
        )
        assertThat(viewModel.stateStack.value.size, `is`(2))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.BoxScore::class.java)
        )
        viewModel.backState()
        assertThat(viewModel.stateStack.value.size, `is`(1))
        assertThat(
            viewModel.currentState.value,
            CoreMatchers.instanceOf(NbaState.Home::class.java)
        )
    }

    @Test
    fun loadData_userNotLogin_checksDataCorrect() = runTest {
        assertThat(viewModel.isLoaded.value, `is`(false))
        viewModel.loadData()
        assertThat(viewModel.isLoaded.value, `is`(true))
        assertThat(repository.getGamesAndBets().first().isNotEmpty(), `is`(true))
    }

    @Test
    fun loadData_userLogin_checksDataCorrect() = runTest {
        assertThat(viewModel.isLoaded.value, `is`(false))
        dataStore.updateUser(
            User(
                account = USER_ACCOUNT,
                name = USER_NAME,
                points = USER_POINTS,
                password = USER_PASSWORD,
                token = ""
            )
        )
        viewModel.loadData()
        assertThat(viewModel.isLoaded.value, `is`(true))
        assertThat(repository.getGamesAndBets().first().isNotEmpty(), `is`(true))
        assertThat(repository.user.value?.account, `is`(USER_ACCOUNT))
        assertThat(repository.user.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun updateState_sameWithCurrentState_checksDataCorrect() {
        val state = NbaState.Bet(
            BetViewModel(
                account = USER_ACCOUNT,
                repository = repository,
                openScreen = {}
            )
        )
        viewModel.updateState(state)
        assertThat(viewModel.currentState.value, `is`(state))
        assertThat(viewModel.stateStack.value.size, `is`(2))
        viewModel.updateState(state)
        assertThat(viewModel.currentState.value, `is`(state))
        assertThat(viewModel.stateStack.value.size, `is`(2))
    }

    @Test
    fun backState_currentHomeState_checksEventSent() {
        assertThat(viewModel.eventFlow.value, nullValue())
        viewModel.backState()
        assertThat(viewModel.eventFlow.value, instanceOf(MainViewModel.Event.Exit::class.java))
    }
}
