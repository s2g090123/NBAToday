package com.jiachian.nbatoday.test

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : BaseUnitTest() {
    @Test
    fun `init() with user had been logged in expects correct`() = launch {
        val user = UserGenerator.get(true)
        dataStore.updateUser(user)
        val viewModel = MainViewModel(
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
        viewModel
            .navigationEvent
            .value
            .assertIsA(NavigationController.Event.NavigateToHome::class.java)
    }

    @Test
    fun `navigationEvent expects initial value is null`() {
        val viewModel = MainViewModel(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController,
            viewModelProvider = composeViewModelProvider,
        )
        viewModel
            .navigationEvent
            .value
            .assertIsNull()
    }

    @Test
    fun `consumeNavigationEvent(Home) expects next event is sent`() {
        val viewModel = MainViewModel(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController,
            viewModelProvider = composeViewModelProvider,
        )
        navigationController.navigateToHome()
        navigationController.navigateToTeam(HomeTeamId)
        viewModel.consumeNavigationEvent(NavigationController.Event.NavigateToHome)
        viewModel
            .navigationEvent
            .value
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
            ?.teamId
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
