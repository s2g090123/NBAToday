package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNot
import com.jiachian.nbatoday.utils.assertIsNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BetViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.user.login(UserAccount, UserPassword)
        viewModel = BetViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Bet.param to UserAccount)),
            repository = get(),
            dispatcherProvider = dispatcherProvider,
        )
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
