package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlin.math.abs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
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
        viewModel = createViewModel(coroutineEnvironment.testDispatcherProvider)
        repository.login(UserAccount, UserPassword)
        repository.refreshSchedule()
        repository.bet(FinalGameId, 0, BasicNumber.toLong())
        repository.bet(PlayingGameId, 0, BasicNumber.toLong())
        repository.bet(ComingSoonGameId, 0, BasicNumber.toLong())
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun bet_clickFinalGame_askTurnTable() = coroutineEnvironment.testScope.runTest {
        viewModel.betAndGame.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betAndGame.value
        val finalGame = betAndGames.firstOrNull { it.game.gameStatus == GameStatusCode.FINAL }
        assertThat(finalGame, notNullValue())
        viewModel.clickBetAndGame(finalGame!!)
        assertThat(viewModel.askTurnTable, notNullValue())
        val points = repository.user.value?.points
        assertThat(points, `is`(UserPoints + BasicNumber * 2))
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
        assertThat(viewModel.isTurnTableStarting.value, `is`(false))
        assertThat(viewModel.currentAngle.value, `is`(0f))
    }

    @Test
    fun bet_showTurnTable_valueCorrect() {
        val data = BetsTurnTableData(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.showTurnTable(data)
        assertThat(viewModel.showTryTurnTable.value, `is`(data))
    }

    @Test
    fun bet_startTurnTable_rewardPointsCorrect() = coroutineEnvironment.testScope.runTest {
        val data = BetsTurnTableData(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.startTurnTable(data)
        advanceUntilIdle()
        val expected = getRewardPoints(
            BasicNumber,
            BasicNumber,
            viewModel.rewardAngle.value
        )
        assertThat(viewModel.showRewardPoints.value, `is`(expected))
    }

    @Test
    fun bet_closeRewardPointsDialog_valueNull() = coroutineEnvironment.testScope.runTest {
        val data = BetsTurnTableData(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.startTurnTable(data)
        advanceUntilIdle()
        assertThat(viewModel.showRewardPoints.value, notNullValue())
        viewModel.closeRewardPointsDialog()
        assertThat(viewModel.showRewardPoints.value, nullValue())
    }

    private fun createViewModel(
        dispatcherProvider: DispatcherProvider
    ): BetViewModel {
        return BetViewModel(
            account = UserAccount,
            repository = repository,
            openScreen = {
                currentState = it
            },
            dispatcherProvider = dispatcherProvider
        )
    }

    private fun getRewardPoints(
        winPoints: Int,
        losePoints: Int,
        angle: Float
    ): Long {
        return when (angle) {
            in 0f..89f -> {
                -abs(winPoints) + abs(losePoints)
            }
            in 90f..179f -> {
                abs(winPoints) * 4
            }
            in 180f..269f -> {
                -abs(winPoints)
            }
            else -> {
                abs(winPoints) + abs(losePoints)
            }
        }.toLong()
    }
}
