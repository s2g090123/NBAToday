package com.jiachian.nbatoday.test.compose.screen.home.user

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.jiachian.nbatoday.AwayTeamColors
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.common.ui.theme.ColorPalette
import com.jiachian.nbatoday.home.user.ui.UserPage
import com.jiachian.nbatoday.home.user.ui.UserPageViewModel
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.team.data.model.local.teams.teamOfficial
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserPageTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: UserPageViewModel

    @Before
    fun setup() {
        viewModel = UserPageViewModel(
            userUseCase = useCaseProvider.user,
        )
        composeTestRule.setContent {
            UserPage(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun clickLogin_loginDialogShown() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.LoginScreen_Button_Login)
                .performClick()
            navigationController.showLoginDialog.assertIsTrue()
        }
    }

    @Test
    fun userPage_checksUI() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.UserTopBar_Text_AccountName)
                .assertTextEquals(UserName)
            onNodeWithUnmergedTree(UserTestTag.UserTopBar_Text_Credits)
                .assertTextEquals(context.getString(R.string.user_points, UserPoints))
            mutableListOf(teamOfficial).apply {
                addAll(NBATeam.nbaTeams)
            }.forEach { team ->
                onNodeWithUnmergedTree(UserTestTag.UserScreen_ThemesTable)
                    .performScrollToNode(hasText(team.teamName))
            }
        }
    }

    @Test
    fun clickBet_moveToBetScreen() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.UserTopBar_Button_Bet)
                .performClick()
            navigationController.toBet.assertIs(UserAccount)
        }
    }

    @Test
    fun clickLogout_checkUserUpdated() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.UserTopBar_Button_Logout)
                .performClick()
            onNodeWithUnmergedTree(UserTestTag.LoginScreen_Button_Login)
                .assertIsDisplayed()
        }
    }

    @Test
    fun clickTheme_checkThemeUpdated() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(UserTestTag.ThemeTable_ThemeCard)
                .onFirst()
                .performClick()
            ColorPalette.assertIs(AwayTeamColors)
        }
    }
}
