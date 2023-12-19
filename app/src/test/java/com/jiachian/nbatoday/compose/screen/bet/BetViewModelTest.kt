package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTablePoints
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.datasource.local.data.TestBetLocalSource
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.data.TestBetRepository
import com.jiachian.nbatoday.repository.data.TestUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModelTest : BaseUnitTest() {
    private lateinit var betRepository: TestBetRepository
    private lateinit var userRepository: TestUserRepository
    private lateinit var navigationController: NavigationController
    private lateinit var viewModel: BetViewModel

    @Before
    fun setup() = runTest {
        navigationController = NavigationController()
        userRepository = TestUserRepository.get().apply {
            login(UserAccount, UserPassword)
        }
        betRepository = TestBetRepository(
            betLocalSource = TestBetLocalSource(),
            userRepository = userRepository
        )
        viewModel = BetViewModel(
            account = UserAccount,
            repository = betRepository,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `clickBetAndGame with Final Game expects turnTablePoints and user's points are updated`() {
        val betAndGame = BetAndGameGenerator.getFinal()
        viewModel.clickBetAndGame(betAndGame)
        val wonPoints = betAndGame.getWonPoints() * 2
        val lostPoints = betAndGame.getLostPoints()
        val updatedPoints = UserPoints + wonPoints
        assertThat(userRepository.user.value?.points, `is`(updatedPoints))
        assertThat(
            viewModel.turnTablePoints.value,
            `is`(TurnTablePoints(wonPoints, lostPoints))
        )
    }

    @Test
    fun `clickBetAndGame with Playing Game expects screen navigates to BoxScore`() {
        val betAndGame = BetAndGameGenerator.getPlaying()
        viewModel.clickBetAndGame(betAndGame)
        assertThat(navigationController.eventFlow.value, instanceOf(NavigationController.Event.NavigateToBoxScore::class.java))
    }

    @Test
    fun `clickBetAndGame with ComingSoon Game expects screen navigates to Team`() {
        val betAndGame = BetAndGameGenerator.getComingSoon()
        viewModel.clickBetAndGame(betAndGame)
        assertThat(navigationController.eventFlow.value, instanceOf(NavigationController.Event.NavigateToTeam::class.java))
    }

    @Test
    fun `closeTurnTable expects all data are reset`() {
        viewModel.closeTurnTable()
        assertThat(viewModel.turnTableVisible.value, `is`(false))
        assertThat(viewModel.turnTablePoints.value, nullValue())
        assertThat(viewModel.turnTableRunning.value, `is`(false))
        assertThat(viewModel.turnTableAngle.value, `is`(0f))
    }

    @Test
    fun `startTurnTable expects rewardedPoints is updated`() = launch {
        val turnTablePoints = TurnTablePoints(BasicNumber.toLong(), BasicNumber.toLong())
        viewModel.startTurnTable(turnTablePoints)
        advanceUntilIdle()
        assertThat(viewModel.turnTableVisible.value, `is`(false))
        assertThat(viewModel.turnTablePoints.value, nullValue())
        assertThat(viewModel.turnTableRunning.value, `is`(false))
        assertThat(viewModel.turnTableAngle.value, `is`(0f))
        assertThat(viewModel.rewardedPoints.value, notNullValue())
        assertThat(userRepository.user.value?.points, not(UserPoints))
    }

    @Test
    fun `closeRewardedPoints expects rewardedPoints is null`() {
        viewModel.closeTurnTable()
        assertThat(viewModel.rewardedPoints.value, nullValue())
    }
}
