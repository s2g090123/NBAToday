package com.jiachian.nbatoday.test

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.rule.emptyRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : BaseUnitTest() {
    /**
     * WORKAROUND!
     * When dealing with #52-collectAsStateWithLifecycle, upgraded the android gradle plugin and gradle versions.
     * After that, started encountering the `io.mockk.MockKException: can't find stub DateUtils(object DateUtils)` problem.
     *
     * Currently unable to confirm the exact cause of the issue,
     * but suspect it may be related to issues with `unmockkObject`,
     * leading to the aforementioned error in the next test.
     *
     * The test doesn't need the CalendarRule,
     * therefore, replace the CalendarRule with an empty TestWatcher.
     */
    override val calendarRule = emptyRule

    @Test
    fun `init() with user had been logged in expects correct`() = launch {
        val user = UserGenerator.get(true)
        dataStore.updateUser(user)
        val event = navigationController.eventFlow.defer(this)
        MainViewModel(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController,
            viewModelProvider = composeViewModelProvider,
            dispatcherProvider = dispatcherProvider
        )
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            .assertIs(user)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToHome::class.java)
    }

    @Test
    fun `consumeNavigationEvent(Home) expects next event is sent`() = launch {
        val viewModel = MainViewModel(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController,
            viewModelProvider = composeViewModelProvider,
        )
        navigationController.navigateToHome()
        val event = viewModel.navigationEvent.defer(this)
        navigationController.navigateToTeam(HomeTeamId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
            .teamId
            .assertIs(HomeTeamId)
    }

    @Test
    fun `viewModelProvider expects correct`() {
        val viewModel = MainViewModel(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController,
            viewModelProvider = composeViewModelProvider,
        )
        viewModel
            .viewModelProvider
            .assertIs(composeViewModelProvider)
    }
}
