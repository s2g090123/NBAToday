package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.BetDialog
import com.jiachian.nbatoday.compose.screen.bet.BetDialogViewModel
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.pressBack
import org.junit.After
import org.junit.Test

class BetDialogTest : BaseAndroidTest() {
    private var homePoints: Long? = null
    private var awayPoints: Long? = null
    private var dialogDismissed: Boolean? = null

    @Composable
    override fun provideComposable(): Any {
        BetDialog(
            viewModel = BetDialogViewModel(
                gameAndBets = GameAndBetsGenerator.getComingSoon(false),
                userPoints = UserPoints,
                dispatcherProvider = dispatcherProvider,
            ),
            onConfirm = { home, away ->
                homePoints = home
                awayPoints = away
            },
            onDismiss = { dialogDismissed = true }
        )
        return super.provideComposable()
    }

    @After
    fun teardown() {
        homePoints = null
        awayPoints = null
        dialogDismissed = null
    }

    @Test
    fun betDialog_expectsUICorrect() = inCompose {
        onNodeWithUnmergedTree(BetTestTag.BetDialog)
            .apply {
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Home).apply {
                    onNodeWithTag(BetTestTag.BetDialogTeamInfo_Text_Record)
                        .assertTextEquals(getString(R.string.bet_win_lose_record, BasicNumber, BasicNumber))
                    onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                        .assertTextEquals("")
                }
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Away).apply {
                    onNodeWithTag(BetTestTag.BetDialogTeamInfo_Text_Record)
                        .assertTextEquals(getString(R.string.bet_win_lose_record, BasicNumber, BasicNumber))
                    onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                        .assertTextEquals("")
                }
                onNodeWithTag(BetTestTag.BetDialogDetail_Text_Remainder)
                    .assertTextEquals(getString(R.string.bet_remain, UserPoints))
                onNodeWithTag(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                    .assertIsNotEnabled()
            }
    }

    @Test
    fun betDialog_betAndConfirm_dialogDismissed() = inCompose {
        onNodeWithUnmergedTree(BetTestTag.BetDialog)
            .apply {
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Away)
                    .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                    .performTextInput(BetPoints.toString())
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Home)
                    .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                    .performTextInput(BetPoints.toString())
                onNodeWithTag(BetTestTag.BetDialogDetail_Text_Remainder)
                    .assertTextEquals(getString(R.string.bet_remain, UserPoints - 2 * BetPoints))
                onNodeWithTag(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                    .performClick()
            }
        onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
            .onNodeWithTag(BetTestTag.BetWarningDialogButtons_Text_Confirm)
            .performClick()
        dialogDismissed.assertIsTrue()
        homePoints.assertIs(BetPoints)
        awayPoints.assertIs(BetPoints)
    }

    @Test
    fun betDialog_cancelWarningDialog_nothingChanged() = inCompose {
        onNodeWithUnmergedTree(BetTestTag.BetDialog)
            .apply {
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Home)
                    .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                    .performTextInput(BetPoints.toString())
                onNodeWithTag(BetTestTag.BetDialogDetail_TeamInfo_Away)
                    .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                    .performTextInput(BetPoints.toString())
                onNodeWithTag(BetTestTag.BetDialogDetail_Text_Remainder)
                    .assertTextEquals(getString(R.string.bet_remain, UserPoints - 2 * BetPoints))
                onNodeWithTag(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                    .performClick()
            }
        onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
            .apply {
                onNodeWithTag("BetWarningDialogButtons_Text_Cancel")
                    .performClick()
                assertDoesNotExist()
            }
        dialogDismissed.assertIsNull()
        homePoints.assertIsNull()
        awayPoints.assertIsNull()
    }

    @Test
    fun betDialog_backPressed_dialogDismissed() = inCompose {
        pressBack()
        dialogDismissed.assertIsTrue()
    }
}
