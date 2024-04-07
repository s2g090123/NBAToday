package com.jiachian.nbatoday.test.compose.screen.account

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.common.ui.login.LoginDialog
import com.jiachian.nbatoday.common.ui.login.LoginDialogViewModel
import com.jiachian.nbatoday.testing.testtag.UserTestTag
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginDialogTest : BaseAndroidTest() {
    private var dialogDismissed: Boolean? = null
    private lateinit var viewModel: LoginDialogViewModel

    @Before
    fun setup() {
        viewModel = LoginDialogViewModel(
            userUseCase = useCaseProvider.user,
        )
        composeTestRule.setContent {
            LoginDialog(
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
    fun login_checkUserNotNull() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.AccountTextField_TextField)
                .performTextInput(UserAccount)
            onNodeWithUnmergedTree(UserTestTag.PasswordTextField_TextField)
                .performTextInput(UserPassword)
            onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Login)
                .performClick()
            waitForIdle() // Wait until the state changes
            dialogDismissed.assertIsTrue()
            dataStore.user.value.assertIsNotNull()
        }
    }

    @Test
    fun register_checkUserNotNull() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.AccountTextField_TextField)
                .performTextInput(UserAccount)
            onNodeWithUnmergedTree(UserTestTag.PasswordTextField_TextField)
                .performTextInput(UserPassword)
            onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Register)
                .performClick()
            waitForIdle() // Wait until the state changes
            dialogDismissed.assertIsTrue()
            dataStore.user.value.assertIsNotNull()
        }
    }

    @Test
    fun emptyInput_buttonsAreDisabled() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Login)
                .assertIsNotEnabled()
            onNodeWithUnmergedTree(UserTestTag.BottomButtons_Button_Register)
                .assertIsNotEnabled()
        }
    }
}
