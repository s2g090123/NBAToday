package com.jiachian.nbatoday.test

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.SplashViewModel
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : BaseUnitTest() {
    @Test
    fun `init() with user had been logged in expects correct`() = launch {
        val user = UserGenerator.get(true)
        dataStore.updateUser(user)
        val event = navigationController.eventFlow.defer(this)
        SplashViewModel(
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
        val viewModel = SplashViewModel(
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
        val viewModel = SplashViewModel(
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
