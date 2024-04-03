package com.jiachian.nbatoday.test.compose.screen.home.standing

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.standing.ui.StandingPageViewModel
import com.jiachian.nbatoday.home.standing.ui.error.StandingError
import com.jiachian.nbatoday.home.standing.ui.event.StandingDataEvent
import com.jiachian.nbatoday.home.standing.ui.event.StandingUIEvent
import com.jiachian.nbatoday.home.standing.ui.model.StandingSorting
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.team.domain.TeamUseCase
import com.jiachian.nbatoday.team.domain.error.AddTeamsError
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNot
import com.jiachian.nbatoday.utils.assertIsNull
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StandingPageViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: StandingPageViewModel

    private lateinit var spyTeamUseCase: TeamUseCase

    @Before
    fun setup() {
        spyTeamUseCase = spyk(useCaseProvider.team)
        viewModel = StandingPageViewModel(
            teamUseCase = spyTeamUseCase,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @After
    fun teardown() {
        unmockkObject(spyTeamUseCase)
    }

    @Test
    fun `onEvent with Refresh and check teams are updated`() {
        viewModel.onEvent(StandingUIEvent.Refresh)
        viewModel.state.eastTeamState.teams.assertIsNot(emptyList())
        viewModel.state.westTeamState.teams.assertIsNot(emptyList())
    }

    @Test
    fun `onEvent with Refresh but failed and check event is Error`() {
        every {
            spyTeamUseCase.addTeams()
        } returns flow { emit(Resource.Error(AddTeamsError.ADD_FAILED)) }
        viewModel.onEvent(StandingUIEvent.Refresh)
        viewModel.state.event
            .assertIsA<StandingDataEvent.Error>()
            .error
            .assertIs(StandingError.RefreshFailed)
    }

    @Test
    fun `onEvent with UpdateSorting and check sorting is updated`() {
        viewModel.onEvent(StandingUIEvent.UpdateSorting(StandingSorting.PTS))
        viewModel.state.eastTeamState.sorting.assertIs(StandingSorting.PTS)
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(StandingUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }
}
