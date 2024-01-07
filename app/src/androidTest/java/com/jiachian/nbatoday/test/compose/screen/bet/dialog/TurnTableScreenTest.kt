package com.jiachian.nbatoday.test.compose.screen.bet.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.dialog.TurnTableScreen
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertDialogDoesNotExist
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import org.junit.Test

class TurnTableScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: BetViewModel

    @Composable
    override fun provideComposable(): Any {
        viewModel = BetViewModel(
            account = UserAccount,
            repository = repositoryProvider.bet,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider
        )
        TurnTableScreen(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun turnTableScreen_checksAskTurnTableUI() = inCompose {
        viewModel.clickBetAndGame(BetAndGameGenerator.getFinal())
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
        viewModel.clickBetAndGame(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Cancel)
            .performClick()
        assertDialogDoesNotExist()
    }

    @Test
    fun turnTableScreen_startsTurnTable() = inCompose {
        viewModel.clickBetAndGame(BetAndGameGenerator.getFinal())
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
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable)
            .assertDoesNotExist()
        viewModel.rewardedPoints.value.assertIsNotNull()
    }

    @Test
    fun turnTableScreen_closesTurnTable() = inCompose {
        viewModel.clickBetAndGame(BetAndGameGenerator.getFinal())
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Continue)
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable_Button_Cancel)
            .assertIsDisplayed()
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.BetTurnTable)
            .assertDoesNotExist()
        viewModel.rewardedPoints.value.assertIsNull()
    }
}
