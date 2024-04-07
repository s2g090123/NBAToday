package com.jiachian.nbatoday.test.compose.screen.bet.dialog

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.bet.ui.turntable.TurnTableScreen
import com.jiachian.nbatoday.bet.ui.turntable.model.Lose
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus
import com.jiachian.nbatoday.bet.ui.turntable.model.Win
import com.jiachian.nbatoday.bet.ui.turntable.state.MutableTurnTableState
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import org.junit.After
import org.junit.Test

class TurnTableScreenTest : BaseAndroidTest() {
    private var status: TurnTableStatus? = null

    @After
    fun teardown() {
        status = null
    }

    @Test
    fun askTurnTableDialog_continue() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.Asking(Win(BetPoints), Lose(BetPoints))
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.AskTurnTableDialog_Text_Body)
                .assertTextEquals(
                    context.resources.getQuantityString(
                        R.plurals.bet_ask_turn_table_text,
                        BetPoints.toInt(),
                        BetPoints,
                        BetPoints
                    )
                )
            onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Continue)
                .performClick()
            status
                .assertIsA<TurnTableStatus.TurnTable>()
                .assertIsTrue { it.win == Win(BetPoints) }
                .assertIsTrue { it.lose == Lose(BetPoints) }
        }
    }

    @Test
    fun askTurnTableDialog_cancel() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.Asking(Win(BetPoints), Lose(BetPoints))
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.AskTurnTableButtons_Text_Cancel)
                .performClick()
            status.assertIs(TurnTableStatus.Idle)
        }
    }

    @Test
    fun betTurnTable_startButton() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.TurnTable(Win(BetPoints), Lose(BetPoints))
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.TurnTableStartButton_Text_Start)
                .performClick()
            status
                .assertIsA<TurnTableStatus.TurnTable>()
                .assertIsTrue { it.win == Win(BetPoints) }
                .assertIsTrue { it.lose == Lose(BetPoints) }
        }
    }

    @Test
    fun betTurnTable_cancelButton() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.TurnTable(Win(BetPoints), Lose(BetPoints))
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetTurnTable_Button_Cancel)
                .performClick()
            status.assertIs(TurnTableStatus.Idle)
        }
    }

    @Test
    fun rewardedDialog_winsPoints() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.Rewarded(BetPoints)
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.RewardedPointsDialog_Text_Title)
                .assertTextEquals(getString(R.string.bet_reward_win_title))
            onNodeWithUnmergedTree(BetTestTag.RewardedPointsDialog_Text_Body)
                .assertTextEquals(
                    context.resources.getQuantityString(
                        R.plurals.bet_reward_win_text,
                        BetPoints.toInt(),
                        BetPoints,
                        BetPoints
                    )
                )
            onNodeWithUnmergedTree(BetTestTag.RewardPointDialog_Text_OK)
                .performClick()
            status.assertIs(TurnTableStatus.Idle)
        }
    }

    @Test
    fun rewardedDialog_losesPoints() {
        composeTestRule.setContent {
            TurnTableScreen(
                state = MutableTurnTableState().apply {
                    status = TurnTableStatus.Rewarded(-BetPoints)
                },
                onOpenTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onStartTurnTable = { win, lose ->
                    status = TurnTableStatus.TurnTable(win, lose)
                },
                onClose = {
                    status = TurnTableStatus.Idle
                }
            )
        }

        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.RewardedPointsDialog_Text_Title)
                .assertTextEquals(getString(R.string.bet_reward_lose_title))
            onNodeWithUnmergedTree(BetTestTag.RewardedPointsDialog_Text_Body)
                .assertTextEquals(
                    context.resources.getQuantityString(
                        R.plurals.bet_reward_lose_text,
                        BetPoints.toInt(),
                        BetPoints,
                        BetPoints
                    )
                )
            onNodeWithUnmergedTree(BetTestTag.RewardPointDialog_Text_OK)
                .performClick()
            status.assertIs(TurnTableStatus.Idle)
        }
    }
}
