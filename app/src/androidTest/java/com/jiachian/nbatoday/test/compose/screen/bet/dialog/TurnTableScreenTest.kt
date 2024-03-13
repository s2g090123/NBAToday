package com.jiachian.nbatoday.test.compose.screen.bet.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.screen.bet.turntable.TurnTableRewardedDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.TurnTableScreen
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertDialogDoesNotExist
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import org.junit.Before
import org.junit.Test

class TurnTableScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: BetViewModel

    @Before
    fun setup() {
        viewModel = BetViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Bet.param to UserAccount)),
            repository = repositoryProvider.bet,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Composable
    override fun ProvideComposable() {
        TurnTableScreen(
            uiState = viewModel.turnTableUIState,
            idle = null,
            asking = {
                AskTurnTableDialog(
                    win = it.win,
                    lose = it.lose,
                    onContinue = { viewModel.showTurnTable(it.win, it.lose) },
                    onCancel = viewModel::closeTurnTable
                )
            },
            turntable = {
                BetTurnTable(
                    modifier = Modifier
                        .testTag(BetTestTag.BetTurnTable)
                        .fillMaxSize(),
                    uiState = it,
                    onStart = { viewModel.startTurnTable(it.win, it.lose) },
                    onClose = viewModel::closeTurnTable
                )
            },
            rewarded = {
                TurnTableRewardedDialog(
                    points = it.points,
                    onDismiss = viewModel::closeTurnTable
                )
            }
        )
    }

    @Test
    fun turnTableScreen_checksAskTurnTableUI() = inCompose {
        viewModel.settleBet(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableDialog_Text_Body)
            .assertTextEquals(
                context.resources.getQuantityString(
                    R.plurals.bet_ask_turn_table_text,
                    0,
                    BetPoints * 2,
                    BetPoints
                )
            )
    }

    @Test
    fun turnTableScreen_closesAskTurnTable() = inCompose {
        viewModel.settleBet(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Cancel)
            .performClick()
        assertDialogDoesNotExist()
    }

    @Test
    fun turnTableScreen_startsTurnTable() = inCompose {
        viewModel.settleBet(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Continue)
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable_Button_Cancel)
            .assertIsDisplayed()
        onNodeWithUnmergedTree(BetTestTag.TurnTableStartButton_Text_Start)
            .assertIsDisplayed()
            .performClick()
            .assertDoesNotExist()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable_Button_Cancel)
            .assertDoesNotExist()
        awaitIdle()
        waitUntil(1000) { viewModel.turnTableUIState !is TurnTableUIState.TurnTable }
        viewModel.turnTableUIState.assertIsA(TurnTableUIState.Rewarded::class.java)
    }

    @Test
    fun turnTableScreen_closesTurnTable() = inCompose {
        viewModel.settleBet(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Continue)
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable_Button_Cancel)
            .assertIsDisplayed()
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable)
            .assertDoesNotExist()
        viewModel.turnTableUIState.assertIs(TurnTableUIState.Idle)
    }
}
