package com.jiachian.nbatoday.compose.screen.home

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {
    private val homePageImp = MutableStateFlow(HomePage.SCHEDULE)
    val homePage = homePageImp.asStateFlow()

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

    fun updateHomePage(page: HomePage) {
        homePageImp.value = page
    }

    override fun close() {
        coroutineScope.cancel()
    }
}
