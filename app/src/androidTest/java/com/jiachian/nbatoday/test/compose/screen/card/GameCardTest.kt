package com.jiachian.nbatoday.test.compose.screen.card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.HomeTeamColors
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.game.ui.GameCard
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.onDialogWithUnMergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameCardTest : BaseAndroidTest() {
    @Test
    fun gameCard_nonExpandable_checksUI() {
        composeTestRule.setContent {
            GameCard(
                uiData = GameCardUIData(
                    gameAndBets = GameAndBetsGenerator.getFinal(),
                    betRepository = repositoryProvider.bet,
                    userRepository = repositoryProvider.user,
                    dispatcherProvider = dispatcherProvider
                ),
                color = HomeTeamColors.primary,
                expandable = false
            )
        }
        composeTestRule
            .onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Expand)
            .assertDoesNotExist()
    }

    @Test
    fun gameCard_expandable_checksUI() {
        composeTestRule.setContent {
            GameCard(
                uiData = GameCardUIData(
                    gameAndBets = GameAndBetsGenerator.getFinal(),
                    betRepository = repositoryProvider.bet,
                    userRepository = repositoryProvider.user,
                    dispatcherProvider = dispatcherProvider
                ),
                color = HomeTeamColors.primary,
                expandable = true
            )
        }
        composeTestRule.apply {
            onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Expand)
                .assertIsDisplayed()
                .performClick()
            onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Collapse)
                .assertIsDisplayed()
        }
    }

    @Test
    fun gameCard_clicksBetButton_displayLoginDialog() {
        composeTestRule.setContent {
            GameCard(
                uiData = GameCardUIData(
                    gameAndBets = GameAndBetsGenerator.getComingSoon(false),
                    betRepository = repositoryProvider.bet,
                    userRepository = repositoryProvider.user,
                    dispatcherProvider = dispatcherProvider
                ),
                color = HomeTeamColors.primary,
                expandable = false
            )
        }
        composeTestRule.apply {
            onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
                .performClick()
            onDialogWithUnMergedTree()
                .onNodeWithTag(UserTestTag.LoginDialog)
                .assertIsDisplayed()
        }
    }

    @Test
    fun gameCard_clicksBetButton_hasBet() {
        val viewModel = spyk(
            GameCardUIData(
                gameAndBets = GameAndBetsGenerator.getComingSoon(false),
                betRepository = repositoryProvider.bet,
                userRepository = repositoryProvider.user,
                dispatcherProvider = dispatcherProvider
            )
        ) {
            every {
                hasBet
            } returns MutableStateFlow(true)
        }
        composeTestRule.setContent {
            GameCard(
                uiData = viewModel,
                color = HomeTeamColors.primary,
                expandable = false
            )
        }
        composeTestRule.apply {
            onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
                .performClick()
        }
    }

    @Test
    fun gameCard_clicksBetButton_displayBetDialog() = launch {
        composeTestRule.setContent {
            GameCard(
                uiData = GameCardUIData(
                    gameAndBets = GameAndBetsGenerator.getComingSoon(false),
                    betRepository = repositoryProvider.bet,
                    userRepository = repositoryProvider.user,
                    dispatcherProvider = dispatcherProvider
                ),
                color = HomeTeamColors.primary,
                expandable = false
            )
        }
        repositoryProvider.user.login(UserAccount, UserPassword)
        composeTestRule.apply {
            onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
                .performClick()
            onDialogWithUnMergedTree()
                .onNodeWithTag(BetTestTag.BetDialog)
                .assertIsDisplayed()
        }
    }
}
