package com.jiachian.nbatoday.test.compose.screen.team

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.team.ui.main.TeamViewModel
import com.jiachian.nbatoday.team.ui.main.event.TeamUIEvent
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerSorting
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TeamViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: TeamViewModel

    @Before
    fun setup() {
        viewModel = TeamViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Team.param to "${TeamGenerator.getHome().teamId}")),
            teamUseCase = useCaseProvider.team,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(TeamUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }

    @Test
    fun `onEvent with Sort and check rowData is sorted`() {
        useCaseProvider.team.addTeams().launchAndCollect()
        viewModel.onEvent(TeamUIEvent.Sort(TeamPlayerSorting.GP))
        viewModel.state.players.sorting.assertIs(TeamPlayerSorting.GP)
        viewModel.state.players.players
            .assertIsTrue { it[0].player.gamePlayed < it[1].player.gamePlayed }
    }
}
