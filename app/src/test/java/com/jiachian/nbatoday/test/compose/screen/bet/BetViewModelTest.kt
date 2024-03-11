package com.jiachian.nbatoday.test.compose.screen.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNot
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BetViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.user.login(UserAccount, UserPassword)
        viewModel = BetViewModel(
            account = UserAccount,
            repository = repositoryProvider.bet,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `clickBetAndGame(finalGame) expects turnTablePoints and user's points are updated`() {
        val betAndGame = BetAndGameGenerator.getFinal()
        viewModel.clickBetAndGame(betAndGame)
        val wonPoints = betAndGame.getWonPoints() * 2
        val lostPoints = betAndGame.getLostPoints()
        val updatedPoints = UserPoints + wonPoints
        assertIs(dataHolder.user.value?.points, updatedPoints)
        viewModel
            .turnTableUIState
            .assertIsA(TurnTableUIState.Asking::class.java)
            .assertIsTrue { it.win == Win(wonPoints) }
            .assertIsTrue { it.lose == Lose(lostPoints) }
    }

    @Test
    fun `clickBetAndGame(playingGame) expects screen navigates to BoxScore`() = launch {
        val betAndGame = BetAndGameGenerator.getPlaying()
        val event = navigationController.eventFlow.defer(this)
        viewModel.clickBetAndGame(betAndGame)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
    }

    @Test
    fun `clickBetAndGame(comingSoonGame) expects screen navigates to Team`() = launch {
        val betAndGame = BetAndGameGenerator.getComingSoon()
        val event = navigationController.eventFlow.defer(this)
        viewModel.clickBetAndGame(betAndGame)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
    }

    @Test
    fun `closeTurnTable() expects all data are reset`() {
        viewModel.closeTurnTable()
        viewModel.turnTableUIState.assertIs(TurnTableUIState.Idle)
    }

    @Test
    fun `startTurnTable() expects rewardedPoints is updated`() = launch {
        viewModel.showTurnTable(Win(BasicNumber.toLong()), Lose(BasicNumber.toLong() + 1))
        viewModel.startTurnTable(Win(BasicNumber.toLong()), Lose(BasicNumber.toLong() + 1))
        advanceUntilIdle()
        viewModel
            .turnTableUIState
            .assertIsA(TurnTableUIState.Rewarded::class.java)
        dataHolder
            .user
            .value
            ?.points
            .assertIsNotNull()
            .assertIsNot(UserPoints)
    }
}
