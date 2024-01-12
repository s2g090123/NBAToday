package com.jiachian.nbatoday.compose.screen.home

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.home.navigation.HomePage
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import kotlinx.coroutines.CoroutineScope

/**
 * ViewModel for handling business logic related to [HomeScreen].
 *
 * @property composeViewModelProvider The provider for creating ComposeViewModel instances.
 * @property navigationController The controller for navigation within the app.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with unconfined dispatcher).
 */
class HomeViewModel(
    private val composeViewModelProvider: ComposeViewModelProvider,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = MainRoute.Home
) {
    // list of pages in the home screen
    val pages = HomePage.values()

    val schedulePageViewModel by lazy {
        composeViewModelProvider.getSchedulePageViewModel(
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    val standingPageViewModel by lazy {
        composeViewModelProvider.getStandingPageViewModel(
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope,
        )
    }

    val userPageViewModel by lazy {
        composeViewModelProvider.getUserPageViewModel(
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }
}
