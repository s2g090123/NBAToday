package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.common.ui.bet.BetDialog
import com.jiachian.nbatoday.common.ui.bet.BetDialogViewModel
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetDialogTest : BaseAndroidTest() {
    private var dialogDismissed: Boolean? = null
    private lateinit var viewModel: BetDialogViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        viewModel = BetDialogViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.BetDialog.param to ComingSoonGameId)),
            betUseCase = useCaseProvider.bet,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
        )
        composeTestRule.setContent {
            BetDialog(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                onDismiss = { dialogDismissed = true },
            )
        }
    }

    @After
    fun teardown() {
        dialogDismissed = null
    }

    @Test
    fun betDialog_checksUI() {
        val game = GameGenerator.getComingSoon()
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Home)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_Text_Record)
                .assertTextEquals(context.getString(R.string.bet_win_lose_record, game.homeTeam.wins, game.homeTeam.losses))
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Away)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_Text_Record)
                .assertTextEquals(context.getString(R.string.bet_win_lose_record, game.homeTeam.wins, game.homeTeam.losses))
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, UserPoints))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsNotEnabled()
        }
    }

    @Test
    fun betDialog_typeHomePoints_confirm() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Home)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .performTextInput("$BetPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, UserPoints - BetPoints))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
                .assertIsDisplayed()
        }
    }

    @Test
    fun betDialog_typeAwayPoints_confirm() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Away)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .performTextInput("$BetPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, UserPoints - BetPoints))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
                .assertIsDisplayed()
        }
    }

    @Test
    fun betDialog_typeHomePoints_butOutOfHold() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Home)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .apply { performTextInput("${UserPoints + 1}") }
                .assertTextEquals("$UserPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, 0))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
                .assertIsDisplayed()
        }
    }

    @Test
    fun betDialog_typeAwayPoints_butOutOfHold() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Away)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .apply { performTextInput("${UserPoints + 1}") }
                .assertTextEquals("$UserPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, 0))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
                .assertIsDisplayed()
        }
    }

    @Test
    fun betWarningDialog_confirm() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Home)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .apply { performTextInput("${UserPoints + 1}") }
                .assertTextEquals("$UserPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, 0))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialogButtons_Text_Confirm)
                .performClick()
            waitForIdle() // Wait until the state changes
            dialogDismissed.assertIsTrue()
        }
    }

    @Test
    fun betWarningDialog_cancel() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_TeamInfo_Home)
                .onNodeWithTag(BetTestTag.BetDialogTeamInfo_TextField_Bet)
                .apply { performTextInput("${UserPoints + 1}") }
                .assertTextEquals("$UserPoints")
            onNodeWithUnmergedTree(BetTestTag.BetDialogDetail_Text_Remainder)
                .assertTextContains(context.getString(R.string.bet_remain, 0))
            onNodeWithUnmergedTree(BetTestTag.BetDialogConfirmButton_Text_Confirm)
                .assertIsEnabled()
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialogButtons_Text_Cancel)
                .performClick()
            onNodeWithUnmergedTree(BetTestTag.BetWarningDialog)
                .assertDoesNotExist()
        }
    }
}
