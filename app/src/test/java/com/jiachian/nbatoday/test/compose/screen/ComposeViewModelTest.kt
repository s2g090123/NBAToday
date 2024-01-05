package com.jiachian.nbatoday.test.compose.screen

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import org.junit.Test

class ComposeViewModelTest : BaseUnitTest() {
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: ComposeViewModel

    @Test
    fun `close() with route expects coroutine is canceled and backScreen is triggered`() {
        coroutineScope = CoroutineScope(dispatcherProvider.main)
        viewModel = ComposeViewModel(
            coroutineScope = coroutineScope,
            navigationController = navigationController,
            route = MainRoute.Home,
        )
        viewModel.close()
        assertIsFalse(coroutineScope.isActive)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.BackScreen::class.java
        )
    }

    @Test
    fun `close() with nothing expects coroutine is canceled and backScreen is not triggered`() {
        coroutineScope = CoroutineScope(dispatcherProvider.main)
        viewModel = ComposeViewModel(
            coroutineScope = coroutineScope,
            navigationController = navigationController,
            route = null,
        )
        viewModel.close()
        assertIsFalse(coroutineScope.isActive)
        assertIsNull(navigationController.eventFlow.value)
    }
}
