package com.jiachian.nbatoday.compose.screen

import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

/**
 * Base ViewModel for Composable in this App.
 *
 * @property coroutineScope The coroutine scope associated with the ViewModel.
 * @property navigationController The navigation controller responsible for screen navigation.
 * @property route The route representing the current screen, nullable if not applicable.
 */
open class ComposeViewModel(
    protected val coroutineScope: CoroutineScope,
    protected val navigationController: NavigationController,
    private val route: MainRoute?,
) {
    /**
     * Closes the current screen by canceling the coroutine scope and navigating back if a route is specified.
     */
    open fun close() {
        coroutineScope.cancel()
        route?.also {
            navigationController.backScreen(it)
        }
    }
}
