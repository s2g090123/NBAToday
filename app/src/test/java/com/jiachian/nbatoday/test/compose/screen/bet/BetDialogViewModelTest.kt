package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.common.ui.bet.BetDialogViewModel
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogDataEvent
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogUIEvent
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetDialogViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BetDialogViewModel

    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        viewModel = BetDialogViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.BetDialog.param to FinalGameId)),
            betUseCase = useCaseProvider.bet,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
        )
    }

    @Test
    fun `onEvent with Bet and check event is Done`() = runTest {
        viewModel.onEvent(BetDialogUIEvent.TextHomePoints(1))
        viewModel.onEvent(BetDialogUIEvent.Bet)
        viewModel.state.event.assertIs(BetDialogDataEvent.Done)
    }

    @Test
    fun `onEvent with Confirm and check warning is true`() {
        viewModel.onEvent(BetDialogUIEvent.Confirm)
        viewModel.state.warning.assertIsTrue()
    }

    @Test
    fun `onEvent with CancelConfirm and check warning is false`() {
        viewModel.onEvent(BetDialogUIEvent.CancelConfirm)
        viewModel.state.warning.assertIsFalse()
    }

    @Test
    fun `onEvent with TextHomePoints and check points is updated`() = runTest {
        viewModel.onEvent(BetDialogUIEvent.TextHomePoints(1))
        viewModel.state.homePoints.assertIs(1)
    }

    @Test
    fun `onEvent with TextHomePoints but out of points and check points is updated`() = runTest {
        viewModel.onEvent(BetDialogUIEvent.TextHomePoints(UserPoints + 1))
        viewModel.state.homePoints.assertIs(UserPoints)
    }

    @Test
    fun `onEvent with TextAwayPoints and check points is updated`() = runTest {
        viewModel.onEvent(BetDialogUIEvent.TextAwayPoints(1))
        viewModel.state.awayPoints.assertIs(1)
    }

    @Test
    fun `onEvent with TextAwayPoints but out of points and check points is updated`() = runTest {
        viewModel.onEvent(BetDialogUIEvent.TextAwayPoints(UserPoints + 1))
        viewModel.state.awayPoints.assertIs(UserPoints)
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(BetDialogUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }
}
