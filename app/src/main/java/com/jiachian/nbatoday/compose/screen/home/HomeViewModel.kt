package com.jiachian.nbatoday.compose.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.screen.home.navigation.HomePage
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ComposeViewModelProvider

/**
 * ViewModel for handling business logic related to [HomeScreen].
 *
 * @property composeViewModelProvider The provider for creating ComposeViewModel instances.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 */
class HomeViewModel(
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    // list of pages in the home screen
    val pages = HomePage.values()

    val userPageViewModel by lazy {
        composeViewModelProvider.getUserPageViewModel(
            dispatcherProvider = dispatcherProvider,
            coroutineScope = viewModelScope
        )
    }
}
