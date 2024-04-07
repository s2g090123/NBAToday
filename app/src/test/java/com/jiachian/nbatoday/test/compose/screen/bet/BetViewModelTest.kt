package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.bet.ui.main.BetViewModel
import com.jiachian.nbatoday.bet.ui.main.event.BetUIEvent
import com.jiachian.nbatoday.bet.ui.turntable.model.Lose
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus
import com.jiachian.nbatoday.bet.ui.turntable.model.Win
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BetViewModel

    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    @Before
    fun setup() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        val bet = BetAndGameGenerator.getFinal()
        useCaseProvider.bet.addBet(
            user = UserGenerator.get(true),
            gameId = bet.game.gameId,
            homePoints = bet.bet.homePoints,
            awayPoints = bet.bet.awayPoints,
        )
        viewModel = BetViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Bet.param to UserAccount)),
            betUseCase = useCaseProvider.bet,
            userUseCase = useCaseProvider.user,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `onEvent with CloseTurnTable and check TurnTable status is Idle`() {
        viewModel.onEvent(BetUIEvent.CloseTurnTable)
        viewModel.state.turnTable.status
            .assertIs(TurnTableStatus.Idle)
    }

    @Test
    fun `onEvent with OpenTurnTable and check TurnTable status is TurnTable`() {
        viewModel.onEvent(BetUIEvent.OpenTurnTable(Win(BetPoints), Lose(BetPoints)))
        viewModel.state.turnTable.status
            .assertIsA<TurnTableStatus.TurnTable>()
            .assertIsTrue { it.win.points == BetPoints }
            .assertIsTrue { it.lose.points == BetPoints }
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(BetUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }

    @Test
    fun `onEvent with Settle and check Turntable status is Asking`() = runTest {
        viewModel.onEvent(BetUIEvent.Settle(BetAndGameGenerator.getFinal()))
        viewModel.state.turnTable.status
            .assertIsA<TurnTableStatus.Asking>()
            .assertIsTrue { it.win == Win(BetPoints * 2) }
            .assertIsTrue { it.lose == Lose(BetPoints) }
        useCaseProvider.bet.getBetGames(UserAccount)
            .first()
            .assertIs(emptyList())
    }

    @Test
    fun `onEvent with StartTurnTable and check Turntable status is Asking`() = runTest {
        viewModel.onEvent(BetUIEvent.OpenTurnTable(Win(BetPoints), Lose(BetPoints)))
        viewModel.onEvent(BetUIEvent.StartTurnTable(Win(BetPoints), Lose(BetPoints)))
        advanceUntilIdle()
        viewModel.state.turnTable.status.assertIsA<TurnTableStatus.Rewarded>()
    }

    @Test
    fun `onEvent with StartTurnTable but status not ready and check TurnTable status is Idle`() = runTest {
        viewModel.onEvent(BetUIEvent.StartTurnTable(Win(BetPoints), Lose(BetPoints)))
        viewModel.state.turnTable.status.assertIsA<TurnTableStatus.Idle>()
    }
}
