package com.jiachian.nbatoday.compose.screen

import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

open class ComposeViewModel(
    protected val coroutineScope: CoroutineScope,
    protected val navigationController: NavigationController,
    private val route: Route?,
) {
    open fun close() {
        coroutineScope.cancel()
        route?.also {
            navigationController.backScreen(route)
        }
    }
}
