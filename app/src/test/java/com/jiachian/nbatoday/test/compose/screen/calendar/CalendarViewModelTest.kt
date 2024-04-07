package com.jiachian.nbatoday.test.compose.screen.calendar

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.calendar.ui.event.CalendarUIEvent
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest : BaseUnitTest() {
    companion object {
        private const val GROUPED_KEY_OFFSET = 100
    }

    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: CalendarViewModel

    private val currentCalendar: Calendar
        get() {
            return DateUtils.getCalendar().apply {
                timeInMillis = PlayingGameTimeMs
            }
        }

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = CalendarViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Calendar.param to "${currentCalendar.timeInMillis}")),
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `onEvent with NextMonth and check topBar state is correct`() {
        viewModel.onEvent(CalendarUIEvent.NextMonth)
        val cal = currentCalendar
        cal.add(Calendar.MONTH, 1)
        viewModel.state.topBar
            .assertIsTrue {
                it.index == cal.get(Calendar.YEAR) * GROUPED_KEY_OFFSET + cal.get(Calendar.MONTH)
            }
            .assertIsTrue {
                it.dateString == DateUtils.getDateString(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH)
                )
            }
            .assertIsFalse { it.hasNext }
            .assertIsTrue { it.hasPrevious }
    }

    @Test
    fun `onEvent with PrevMonth and check topBar state is correct`() {
        viewModel.onEvent(CalendarUIEvent.PrevMonth)
        val cal = currentCalendar
        cal.add(Calendar.MONTH, -1)
        viewModel.state.topBar
            .assertIsTrue {
                it.index == cal.get(Calendar.YEAR) * GROUPED_KEY_OFFSET + cal.get(Calendar.MONTH)
            }
            .assertIsTrue {
                it.dateString == DateUtils.getDateString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
            }
            .assertIsFalse { it.hasPrevious }
            .assertIsTrue { it.hasNext }
    }

    @Test
    fun `onEvent with SelectDate and check selectedDate is updated`() {
        val cal = currentCalendar
        cal.add(Calendar.DAY_OF_MONTH, 1)
        viewModel.onEvent(CalendarUIEvent.SelectDate(cal.time))
        viewModel.state.dates.selectedDate.assertIs(cal.time)
    }
}
