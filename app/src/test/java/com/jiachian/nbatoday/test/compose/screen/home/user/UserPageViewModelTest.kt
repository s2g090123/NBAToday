package com.jiachian.nbatoday.test.compose.screen.home.user

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.common.ui.theme.LakersColors
import com.jiachian.nbatoday.home.user.ui.UserPageViewModel
import com.jiachian.nbatoday.home.user.ui.event.UserUIEvent
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.team.data.model.local.teams.teamLakers
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPageViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: UserPageViewModel

    @Before
    fun setup() {
        viewModel = UserPageViewModel(
            userUseCase = useCaseProvider.user,
        )
    }

    @Test
    fun `onEvent with Logout and check state is not login `() {
        viewModel.onEvent(UserUIEvent.Logout)
        viewModel.state.user.assertIsNull()
        viewModel.state.login.assertIsFalse()
    }

    @Test
    fun `onEvent with UpdateTheme and check theme is updated`() {
        viewModel.onEvent(UserUIEvent.UpdateTheme(teamLakers.teamId))
        dataStore.themeColors.value.assertIs(LakersColors)
    }
}
