package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.GameCalendarViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.*
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

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
            repository = repository,
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
                    teamId = TeamStatsFactory.getHomeTeamStats().teamId,
                    repository = repository,
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
                PlayerInfoViewModel(
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
                GameCalendarViewModel(
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
}