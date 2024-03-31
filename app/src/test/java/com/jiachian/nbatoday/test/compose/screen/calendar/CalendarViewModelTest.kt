package com.jiachian.nbatoday.test.compose.screen.calendar

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.get
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: CalendarViewModel

    private val currentCalendar: Calendar
        get() {
            return DateUtils.getCalendar().apply {
                timeInMillis = BasicTime
            }
        }

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = CalendarViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Calendar.param to "$BasicTime")),
            repository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `selectDate() expects selectedDate is updated`() {
        val date = Date()
        viewModel.selectDate(date)
        assertIs(viewModel.selectedDate.value, date)
    }

    @Test
    fun `nextMonth() with hasNextMonth expects numberAndDateString is updated`() {
        viewModel.numberAndDateString.launchAndCollect()
        val spy = spyk(viewModel) {
            every { hasNextMonth() } returns true
        }
        spy.nextMonth()
        val expected = currentCalendar.let { cal ->
            cal.add(Calendar.MONTH, 1)
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            year * 100 + month to DateUtils.getDateString(year, month)
        }
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `nextMonth() with not hasNextMonth expects numberAndDateString is not updated`() {
        val spy = spyk(viewModel) {
            every { hasNextMonth() } returns false
        }
        val expected = spy.numberAndDateString.value
        spy.nextMonth()
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `lastMonth() with hasLastMonth expects numberAndDateString is updated`() {
        viewModel.numberAndDateString.launchAndCollect()
        val spy = spyk(viewModel) {
            every { hasLastMonth() } returns true
        }
        spy.prevMonth()
        val expected = currentCalendar.let { cal ->
            cal.add(Calendar.MONTH, -1)
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            year * 100 + month to DateUtils.getDateString(year, month)
        }
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `lastMonth() with not hasLastMonth expects numberAndDateString is not updated`() {
        val spy = spyk(viewModel) {
            every { hasLastMonth() } returns false
        }
        val expected = spy.numberAndDateString.value
        spy.prevMonth()
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `selectedGamesVisible with selecting today expects visible`() {
        viewModel.gamesVisible.launchAndCollect()
        viewModel.selectDate(Date(BasicTime))
        assertIsTrue(viewModel.gamesVisible.value)
    }

    @Test
    fun `selectedGamesVisible with selecting next two month expects invisible`() {
        viewModel.gamesVisible.launchAndCollect()
        viewModel.selectDate(Date(BasicTime + 24 * 60 * 60 * 1000 * 60L))
        assertIsFalse(viewModel.gamesVisible.value)
    }
}
