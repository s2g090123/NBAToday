package com.jiachian.nbatoday.test.compose.screen.home.schedule

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.schedule.domain.ScheduleUseCase
import com.jiachian.nbatoday.home.schedule.domain.error.UpdateScheduleError
import com.jiachian.nbatoday.home.schedule.ui.SchedulePageViewModel
import com.jiachian.nbatoday.home.schedule.ui.error.ScheduleError
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleDataEvent
import com.jiachian.nbatoday.home.schedule.ui.event.ScheduleUIEvent
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsNot
import com.jiachian.nbatoday.utils.assertIsNull
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SchedulePageViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: SchedulePageViewModel

    private lateinit var spyScheduleUseCase: ScheduleUseCase

    @Before
    fun setup() {
        spyScheduleUseCase = spyk(useCaseProvider.schedule)
        viewModel = SchedulePageViewModel(
            scheduleUseCase = spyScheduleUseCase,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @After
    fun teardown() {
        unmockkObject(spyScheduleUseCase)
    }

    @Test
    fun `onEvent with Refresh and check games are updated`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel.onEvent(ScheduleUIEvent.Refresh)
        dataHolder.games.value.assertIsNot(emptyList())
    }

    @Test
    fun `onEvent with Refresh but error date and check event is Error`() {
        every {
            spyScheduleUseCase.updateSchedule(any(), any(), any())
        } returns flow { emit(Resource.Error(UpdateScheduleError.UPDATE_FAILED)) }
        viewModel.onEvent(ScheduleUIEvent.Refresh)
        viewModel.state.event
            .assertIsA<ScheduleDataEvent.Error>()
            .error
            .assertIs(ScheduleError.RefreshFailed)
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(ScheduleUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }
}
