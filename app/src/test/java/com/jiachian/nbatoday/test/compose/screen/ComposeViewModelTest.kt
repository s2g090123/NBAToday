package com.jiachian.nbatoday.test.compose.screen

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsFalse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.isActive
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ComposeViewModelTest : BaseUnitTest() {
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: ComposeViewModel

    @Test
    fun `close() with route expects coroutine is canceled and backScreen is triggered`() = launch {
        coroutineScope = CoroutineScope(dispatcherProvider.main)
        viewModel = ComposeViewModel(
            coroutineScope = coroutineScope,
            navigationController = navigationController,
            route = MainRoute.Home,
        )
        val event = navigationController.eventFlow.defer(this)
        viewModel.close()
        assertIsFalse(coroutineScope.isActive)
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
    }
}
