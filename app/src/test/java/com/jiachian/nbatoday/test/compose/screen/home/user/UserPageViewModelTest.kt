package com.jiachian.nbatoday.test.compose.screen.home.user

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.data.local.NBATeamGenerator
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNotNull
import com.jiachian.nbatoday.utils.assertIsNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserPageViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: UserPageViewModel

    private val actualUser: User?
        get() = viewModel.userState.value.getDataOrNull()

    @Before
    fun setup() {
        viewModel = UserPageViewModel(
            repository = repositoryProvider.user,
            dataStore = dataStore,
            navigationController = navigationController,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `userState expects user does not log in`() {
        assertIsA(
            viewModel.userState.value,
            UIState.Loading::class.java
        )
        viewModel.userState.launchAndCollect()
        assertIsA(
            viewModel.userState.value,
            UIState.Loaded::class.java
        )
        assertIsNull(actualUser)
    }

    @Test
    fun `teams expects correct`() {
        assertIs(
            viewModel.teams,
            mutableListOf<NBATeam>().apply {
                add(teamOfficial)
                addAll(NBATeam.nbaTeams)
            }
        )
    }

    @Test
    fun `onBetClick() expects screen navigates to Bet`() = launch {
        repositoryProvider.user.login(UserAccount, UserPassword)
        val event = navigationController.eventFlow.defer(this)
        viewModel.onBetClick()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBet::class.java)
    }

    @Test
    fun `updateTheme(Home) expects themeColors is updated`() {
        val team = NBATeamGenerator.getHome()
        viewModel.updateTheme(team)
        val actual = dataStore.themeColors.stateIn(OfficialColors).value
        assertIs(actual, team.colors)
    }

    @Test
    fun `login() expects user logs in`() {
        viewModel.userState.launchAndCollect()
        viewModel.login(UserAccount, UserPassword)
        assertIsNotNull(actualUser)
        assertIs(actualUser?.account, UserAccount)
        assertIs(actualUser?.password, UserPassword)
    }

    @Test
    fun `logout() with login expects user logs out`() {
        viewModel.userState.launchAndCollect()
        viewModel.login(UserAccount, UserPassword)
        viewModel.logout()
        assertIsNull(actualUser)
    }

    @Test
    fun `register() expects user logs in`() {
        viewModel.userState.launchAndCollect()
        viewModel.register(UserAccount, UserPassword)
        assertIsNotNull(actualUser)
        assertIs(actualUser?.account, UserAccount)
        assertIs(actualUser?.password, UserPassword)
    }
}
