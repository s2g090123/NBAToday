package com.jiachian.nbatoday.test.compose.screen.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTablePoints
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNot
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
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
        viewModel = composeViewModelProvider.getBetViewModel(
            account = UserAccount,
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
        assertIs(
            viewModel.turnTablePoints.value,
            TurnTablePoints(wonPoints, lostPoints)
        )
    }

    @Test
    fun `clickBetAndGame(playingGame) expects screen navigates to BoxScore`() {
        val betAndGame = BetAndGameGenerator.getPlaying()
        viewModel.clickBetAndGame(betAndGame)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBoxScore::class.java
        )
    }

    @Test
    fun `clickBetAndGame(comingSoonGame) expects screen navigates to Team`() {
        val betAndGame = BetAndGameGenerator.getComingSoon()
        viewModel.clickBetAndGame(betAndGame)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToTeam::class.java
        )
    }

    @Test
    fun `closeTurnTable() expects all data are reset`() {
        viewModel.closeTurnTable()
        assertIs(viewModel.turnTableVisible.value, false)
        assertIsNull(viewModel.turnTablePoints.value)
        assertIs(viewModel.turnTableRunning.value, false)
        assertIs(viewModel.turnTableAngle.value, 0f)
    }

    @Test
    fun `startTurnTable() expects rewardedPoints is updated`() = launch {
        val turnTablePoints = TurnTablePoints(BasicNumber.toLong(), BasicNumber.toLong() + 1)
        viewModel.startTurnTable(turnTablePoints)
        advanceUntilIdle()
        assertIs(viewModel.turnTableVisible.value, false)
        assertIsNull(viewModel.turnTablePoints.value)
        assertIs(viewModel.turnTableRunning.value, false)
        assertIs(viewModel.turnTableAngle.value, 0f)
        assertIsNotNull(viewModel.rewardedPoints.value)
        assertIsNot(dataHolder.user.value?.points, UserPoints)
    }

    @Test
    fun `closeRewardedPoints expects rewardedPoints is null`() {
        viewModel.closeTurnTable()
        assertIsNull(viewModel.rewardedPoints.value)
    }
}
