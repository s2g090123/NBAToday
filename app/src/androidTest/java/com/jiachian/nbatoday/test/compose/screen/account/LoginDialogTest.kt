package com.jiachian.nbatoday.test.compose.screen.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.common.ui.login.LoginDialog
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.pressBack
import org.junit.After
import org.junit.Test

class LoginDialogTest : BaseAndroidTest() {
    private var user: User? = null
    private var dialogDismissed = false

    @Composable
    override fun ProvideComposable() {
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

    @After
    fun teardown() {
        user = null
        dialogDismissed = false
    }

    @Test
    fun loginDialog_login_userIsUpdated() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.AccountTextField_TextField)
            .performTextInput(UserAccount)
        onNodeWithUnmergedTree(UserTestTag.PasswordTextField_TextField)
            .performTextInput(UserPassword)
        onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Login)
            .performClick()
        user
            .assertIsNotNull()
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun loginDialog_loginInvalidInput_nothingChanged() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Login)
            .performClick()
        user.assertIsNull()
    }

    @Test
    fun loginDialog_register_userIsUpdated() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.AccountTextField_TextField)
            .performTextInput(UserAccount)
        onNodeWithUnmergedTree(UserTestTag.PasswordTextField_TextField)
            .performTextInput(UserPassword)
        onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Register)
            .performClick()
        user
            .assertIsNotNull()
            .assertIsTrue { it?.account == UserAccount }
            .assertIsTrue { it?.password == UserPassword }
    }

    @Test
    fun loginDialog_registerInvalidInput_nothingChanged() = inCompose {
        onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Register)
            .performClick()
        user.assertIsNull()
    }

    @Test
    fun loginDialog_backPressed_dialogDismissed() = inCompose {
        pressBack()
        dialogDismissed.assertIsTrue()
    }
}
