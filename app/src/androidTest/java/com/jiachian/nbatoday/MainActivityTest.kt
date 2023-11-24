package com.jiachian.nbatoday

import androidx.test.core.app.ActivityScenario
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
import com.jiachian.nbatoday.koin.KoinTestRule
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkObject
import java.util.Date
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainActivityTest {

    private val repository = TestRepository()
    private val dataStore = TestDataStore()
    private val viewModel = MainViewModel(repository, dataStore)

    private val testModule = module {
        viewModel { viewModel }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(listOf(testModule))

    @Test
    fun mainActivity_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.close()
    }

    @Test
    fun mainActivity_simulateRefresh_checksEverythingOK() {
        mockkObject(repository)
        coEvery {
            repository.refreshSchedule()
        } answers {
            Thread.sleep(500)
        }
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.close()
        unmockkObject(repository)
    }

    @Test
    fun mainActivity_updateBoxScoreState_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.updateState(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = NbaGameFactory.getFinalGame(),
                    repository = repository,
                    showPlayerCareer = {}
                )
            )
        )
        scenario.close()
    }

    @Test
    fun mainActivity_updateTeamState_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.updateState(
            NbaState.Team(
                TeamViewModel(
                    team = TeamStatsFactory.getHomeTeamStats().team,
                    teamRepository = repository,
                    openScreen = {}
                )
            )
        )
        scenario.close()
    }

    @Test
    fun mainActivity_updatePlayerState_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.updateState(
            NbaState.Player(
                PlayerViewModel(
                    playerId = PlayerStatsFactory.getHomePlayerStats().playerId,
                    repository = repository
                )
            )
        )
        scenario.close()
    }

    @Test
    fun mainActivity_updateCalendarState_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.updateState(
            NbaState.Calendar(
                CalendarViewModel(
                    date = Date(BasicTime),
                    repository = repository,
                    openScreen = {}
                )
            )
        )
        scenario.close()
    }

    @Test
    fun mainActivity_updateBetState_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.updateState(
            NbaState.Bet(
                BetViewModel(
                    account = UserAccount,
                    repository = repository,
                    openScreen = {}
                )
            )
        )
        scenario.close()
    }

    @Test
    fun mainActivity_exitApp_checksEverythingOK() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        viewModel.backState()
        scenario.close()
    }
}
