package com.jiachian.nbatoday.test.compose.screen.home.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.home.user.UserPage
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.theme.ColorPalette
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onDialogWithUnMergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.pressBack
import org.junit.Test

class UserPageTest : BaseAndroidTest() {
    @Composable
    override fun provideComposable(): Any {
        UserPage(
            viewModel = UserPageViewModel(
                repository = repositoryProvider.user,
                dataStore = dataStore,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider,
            )
        )
        return super.provideComposable()
    }

    @Test
    fun userPage_checksLoginScreen_clicksLogin() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.UserPage_LoginScreen)
            .assertIsDisplayed()
            .onNodeWithTag(UserTestTag.LoginScreen_Button_Login)
            .performClick()
        onDialogWithUnMergedTree()
            .onNodeWithTag(UserTestTag.LoginDialog)
            .assertIsDisplayed()
            .apply {
                onNodeWithTag(UserTestTag.AccountTextField_TextField)
                    .performTextInput(UserAccount)
                onNodeWithTag(UserTestTag.PasswordTextField_TextField)
                    .performTextInput(UserPassword)
                onNodeWithTag(UserTestTag.BottomButtons_Button_Login)
                    .performClick()
            }
        onDialogWithUnMergedTree()
            .assertDoesNotExist()
        dataHolder
            .user
            .value
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun userPage_checksLoginScreen_clicksRegister() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.UserPage_LoginScreen)
            .assertIsDisplayed()
            .onNodeWithTag(UserTestTag.LoginScreen_Button_Login)
            .performClick()
        onDialogWithUnMergedTree()
            .onNodeWithTag(UserTestTag.LoginDialog)
            .assertIsDisplayed()
            .apply {
                onNodeWithTag(UserTestTag.AccountTextField_TextField)
                    .performTextInput(UserAccount)
                onNodeWithTag(UserTestTag.PasswordTextField_TextField)
                    .performTextInput(UserPassword)
                onNodeWithTag(UserTestTag.BottomButtons_Button_Register)
                    .performClick()
            }
        onDialogWithUnMergedTree()
            .assertDoesNotExist()
        dataHolder
            .user
            .value
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun userPage_checksLoginScreen_bakcPressed() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.UserPage_LoginScreen)
            .assertIsDisplayed()
            .onNodeWithTag(UserTestTag.LoginScreen_Button_Login)
            .performClick()
        onDialogWithUnMergedTree()
            .onNodeWithTag(UserTestTag.LoginDialog)
            .assertIsDisplayed()
        pressBack()
        onDialogWithUnMergedTree()
            .assertDoesNotExist()
        dataHolder.user.value.assertIsNull()
    }

    @Test
    fun userPage_checksUserScreen() = inCompose {
        repositoryProvider.user.login(UserAccount, UserPassword)
        onNodeWithUnmergedTree(UserTestTag.UserTopBar_Text_AccountName)
            .assertTextEquals(UserName)
        onNodeWithUnmergedTree(UserTestTag.UserTopBar_Text_Credits)
            .assertTextEquals(getString(R.string.user_points, UserPoints))
        mutableListOf(teamOfficial).apply {
            addAll(NBATeam.nbaTeams)
        }.forEach { team ->
            onNodeWithUnmergedTree(UserTestTag.UserScreen_ThemesTable)
                .performScrollToNode(hasText(team.teamName))
        }
    }

    @Test
    fun userPage_clicksThemeCard() = inCompose {
        repositoryProvider.user.login(UserAccount, UserPassword)
        onAllNodesWithUnmergedTree(UserTestTag.ThemeTable_ThemeCard)[0]
            .performClick()
        ColorPalette.assertIs(OfficialColors)
    }

    @Test
    fun userPage_clicksBet_switchesToBetScreen() = inCompose {
        repositoryProvider.user.login(UserAccount, UserPassword)
        onNodeWithUnmergedTree(UserTestTag.UserTopBar_Button_Bet)
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.NavigateToBet::class.java)
            ?.account
            .assertIs(UserAccount)
    }

    @Test
    fun userPage_clicksLogout_loginScreenVisible() = inCompose {
        repositoryProvider.user.login(UserAccount, UserPassword)
        onNodeWithUnmergedTree(UserTestTag.UserTopBar_Button_Logout)
            .performClick()
        dataHolder.user.value.assertIsNull()
        onNodeWithUnmergedTree(UserTestTag.UserPage_LoginScreen)
            .assertIsDisplayed()
    }
}
