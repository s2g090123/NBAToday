package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTablePoints
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.models.local.game.GameStatus
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
        viewModel.betsAndGames.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betsAndGames.value
        val finalGame = betAndGames.firstOrNull { it.game.gameStatus == GameStatus.FINAL }
        assertThat(finalGame, notNullValue())
        viewModel.clickBetAndGame(finalGame!!)
        assertThat(viewModel.turnTablePoints, notNullValue())
        val points = repository.user.value?.points
        assertThat(points, `is`(UserPoints + BasicNumber * 2))
        assertThat(viewModel.betsAndGames.value.contains(finalGame), `is`(false))
    }

    @Test
    fun bet_clickPlayingGame_openBoxScore() {
        viewModel.betsAndGames.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betsAndGames.value
        val playingGame = betAndGames.firstOrNull { it.game.gameStatus == GameStatus.PLAYING }
        assertThat(playingGame, notNullValue())
        viewModel.clickBetAndGame(playingGame!!)
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun bet_clickComingSoonGame_openTeamScreen() {
        viewModel.betsAndGames.launchAndCollect(coroutineEnvironment)
        val betAndGames = viewModel.betsAndGames.value
        val comingGame =
            betAndGames.firstOrNull { it.game.gameStatus == GameStatus.COMING_SOON }
        assertThat(comingGame, notNullValue())
        viewModel.clickBetAndGame(comingGame!!)
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun bet_closeAskTurnTable_valueNull() {
        viewModel.closeAskTurnTable()
        assertThat(viewModel.turnTablePoints.value, nullValue())
    }

    @Test
    fun bet_closeTurnTable_valueNull() {
        viewModel.closeTurnTable()
        assertThat(viewModel.tryTurnTableVisible.value, nullValue())
        assertThat(viewModel.turnTableRunning.value, `is`(false))
        assertThat(viewModel.turnTableAngle.value, `is`(0f))
    }

    @Test
    fun bet_showTurnTable_valueCorrect() {
        val data = TurnTablePoints(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.showTurnTable(data)
        assertThat(viewModel.tryTurnTableVisible.value, `is`(data))
    }

    @Test
    fun bet_startTurnTable_rewardPointsCorrect() = coroutineEnvironment.testScope.runTest {
        val data = TurnTablePoints(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.startTurnTable(data)
        advanceUntilIdle()
        val expected = getRewardPoints(
            BasicNumber,
            BasicNumber,
            viewModel.rewardedAngle.value
        )
        assertThat(viewModel.rewardedPoints.value, `is`(expected))
    }

    @Test
    fun bet_closeRewardPointsDialog_valueNull() = coroutineEnvironment.testScope.runTest {
        val data = TurnTablePoints(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.startTurnTable(data)
        advanceUntilIdle()
        assertThat(viewModel.rewardedPoints.value, notNullValue())
        viewModel.closeRewardedPoints()
        assertThat(viewModel.rewardedPoints.value, nullValue())
    }

    private fun createViewModel(
        dispatcherProvider: DispatcherProvider
    ): BetViewModel {
        return BetViewModel(
            account = UserAccount,
            betRepository = repository,
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
