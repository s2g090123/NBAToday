package com.jiachian.nbatoday.test

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.compose.screen.splash.SplashViewModel
import com.jiachian.nbatoday.data.local.UserGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : BaseUnitTest() {
    @Test
    fun `init() with user had been logged in expects correct`() = launch {
        val user = UserGenerator.get(true)
        dataStore.updateUser(user)
        val viewModel = SplashViewModel(
            scheduleRepository = get(),
            teamRepository = get(),
            userRepository = get(),
            dataStore = get(),
            dispatcherProvider = dispatcherProvider,
        )
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            .assertIs(user)
        viewModel.isLoaded.assertIsTrue()
    }
}
