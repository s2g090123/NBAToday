package com.jiachian.nbatoday.test.compose.screen.account

import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginDialogTest : BaseAndroidTest() {
    private var user: User? = null
    private var dialogDismissed = false

    @Before
    fun setup() {
        composeTestRule.setContent {
            LoginDialog(
                onLogin = { account, password ->
                    user = UserGenerator.get(true).copy(
                        account = account,
                        password = password
                    )
                },
                onRegister = { account, password ->
                    user = UserGenerator.get(true).copy(
                        account = account,
                        password = password
                    )
                },
                onDismiss = { dialogDismissed = true }
            )
        }
    }

    @After
    fun teardown() {
        user = null
        dialogDismissed = false
    }

    @Test
    fun loginDialog_login_userIsUpdated() {
        composeTestRule.apply {
            onNodeWithMergedTag(UserTestTag.AccountTextField_TextField)
                .performTextInput(UserAccount)
            onNodeWithMergedTag(UserTestTag.PasswordTextField_TextField)
                .performTextInput(UserPassword)
            onNodeWithMergedTag(UserTestTag.BottomButtons_Button_Login)
                .performClick()
        }
        user
            .assertIsNotNull()
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun loginDialog_loginInvalidInput_nothingChanged() {
        composeTestRule.apply {
            onNodeWithMergedTag(UserTestTag.BottomButtons_Button_Login)
                .performClick()
        }
        user.assertIsNull()
    }

    @Test
    fun loginDialog_register_userIsUpdated() {
        composeTestRule.apply {
            onNodeWithMergedTag(UserTestTag.AccountTextField_TextField)
                .performTextInput(UserAccount)
            onNodeWithMergedTag(UserTestTag.PasswordTextField_TextField)
                .performTextInput(UserPassword)
            onNodeWithMergedTag(UserTestTag.BottomButtons_Button_Register)
                .performClick()
        }
        user
            .assertIsNotNull()
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun loginDialog_registerInvalidInput_nothingChanged() {
        composeTestRule.apply {
            onNodeWithMergedTag(UserTestTag.BottomButtons_Button_Register)
                .performClick()
        }
        user.assertIsNull()
    }

    @Test
    fun loginDialog_backPressed_dialogDismissed() {
        Espresso.pressBack()
        dialogDismissed.assertIsTrue()
    }
}
