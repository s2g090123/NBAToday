package com.jiachian.nbatoday.test.compose.screen.player

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.player.ui.event.PlayerUIEvent
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setup() = runTest {
        val player = PlayerGenerator.getHome()
        useCaseProvider.player.addPlayer(player.playerId)
        viewModel = PlayerViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Player.param to "${player.playerId}")),
            playerUseCase = useCaseProvider.player,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(PlayerUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }

    @Test
    fun `onEvent with Sort and check rowData is sorted`() {
        viewModel.onEvent(PlayerUIEvent.Sort(PlayerStatsSorting.GP))
        viewModel.state.stats.sorting.assertIs(PlayerStatsSorting.GP)
        viewModel.state.stats.data
            .assertIsTrue { it[0].stats.gamePlayed < it[1].stats.gamePlayed }
    }
}
