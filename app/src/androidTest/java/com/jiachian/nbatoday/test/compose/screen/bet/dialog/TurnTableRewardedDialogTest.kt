package com.jiachian.nbatoday.test.compose.screen.bet.dialog

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.turntable.TurnTableRewardedDialog
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import org.junit.After
import org.junit.Test

class TurnTableRewardedDialogTest : BaseAndroidTest() {
    private var dismissed: Boolean? = null

    @After
    fun teardown() {
        dismissed = null
    }

    @Test
    fun rewardedPointsScreen_earnsPoints_checksUI() {
        composeTestRule.setContent {
            TurnTableRewardedDialog(
                points = BetPoints,
                onDismiss = { dismissed = true }
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
                        BetPoints
                    )
                )
        }
    }

    @Test
    fun rewardedPointsScreen_losesPoints_checksUI() {
        composeTestRule.setContent {
            TurnTableRewardedDialog(
                points = -BetPoints,
                onDismiss = { dismissed = true }
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
                        BetPoints
                    )
                )
        }
    }

    @Test
    fun rewardedPointsScreen_closesDialog() {
        composeTestRule.setContent {
            TurnTableRewardedDialog(
                points = BetPoints,
                onDismiss = { dismissed = true }
            )
        }
        composeTestRule
            .onNodeWithUnmergedTree(BetTestTag.RewardPointDialog_Text_OK)
            .performClick()
        dismissed.assertIsTrue()
    }
}
