package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModelTest {

    private var currentState: NbaState? = null
    private val repository = TestRepository()
    private lateinit var viewModel: BetViewModel
    private val coroutineEnvironment = TestCoroutineEnvironment()

    @Before
    fun setup() = runTest {
        viewModel = createViewModel(
            coroutineEnvironment.testScope,
            coroutineEnvironment.testDispatcherProvider
        )
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.refreshSchedule()
        repository.bet(FINAL_GAME_ID, 0, BASIC_NUMBER.toLong())
        repository.bet(PLAYING_GAME_ID, 0, BASIC_NUMBER.toLong())
        repository.bet(COMING_SOON_GAME_ID, 0, BASIC_NUMBER.toLong())
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun bet_clickFinalGame_askTurnTable() = runTest {
        viewModel.betAndGame.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betAndGame.value
        val finalGame = betAndGames.firstOrNull { it.game.gameStatus == GameStatusCode.FINAL }
        assertThat(finalGame, notNullValue())
        viewModel.clickBetAndGame(finalGame!!)
        assertThat(viewModel.askTurnTable, notNullValue())
        val points = repository.user.value?.points
        assertThat(points, `is`(USER_POINTS + BASIC_NUMBER * 2))
        assertThat(viewModel.betAndGame.value.contains(finalGame), `is`(false))
    }

    @Test
    fun bet_clickPlayingGame_openBoxScore() {
        viewModel.betAndGame.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betAndGame.value
        val playingGame = betAndGames.firstOrNull { it.game.gameStatus == GameStatusCode.PLAYING }
        assertThat(playingGame, notNullValue())
        viewModel.clickBetAndGame(playingGame!!)
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun bet_clickComingSoonGame_openTeamScreen() {
        viewModel.betAndGame.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betAndGame.value
        val comingGame =
            betAndGames.firstOrNull { it.game.gameStatus == GameStatusCode.COMING_SOON }
        assertThat(comingGame, notNullValue())
        viewModel.clickBetAndGame(comingGame!!)
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun bet_closeAskTurnTable_valueNull() {
        viewModel.closeAskTurnTable()
        assertThat(viewModel.askTurnTable.value, nullValue())
    }

    @Test
    fun bet_closeTurnTable_valueNull() {
        viewModel.closeTurnTable()
        assertThat(viewModel.showTryTurnTable.value, nullValue())
    }

    @Test
    fun bet_startTurnTable_valueCorrect() {
        val data = BetsTurnTableData(BASIC_NUMBER.toLong(), BASIC_NUMBER.toLong())
        viewModel.startTurnTable(data)
        assertThat(viewModel.showTryTurnTable.value, `is`(data))
    }

    @Test
    fun addPoints() = runTest {
        viewModel.addPoints(BASIC_NUMBER.toLong())
        assertThat(
            repository.user.value?.points,
            `is`(USER_POINTS + BASIC_NUMBER)
        )
    }

    private fun createViewModel(
        coroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider
    ): BetViewModel {
        return BetViewModel(
            account = USER_ACCOUNT,
            repository = repository,
            openScreen = {
                currentState = it
            },
            coroutineScope = coroutineScope,
            dispatcherProvider = dispatcherProvider
        )
    }
}