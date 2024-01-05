package com.jiachian.nbatoday.compose.screen

import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

open class ComposeViewModel(
    protected val coroutineScope: CoroutineScope,
    protected val navigationController: NavigationController,
    private val route: MainRoute?,
) {
    open fun close() {
        coroutineScope.cancel()
        route?.also {
            navigationController.backScreen(it)
        }
    }
}
