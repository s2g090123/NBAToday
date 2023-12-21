package com.jiachian.nbatoday.test.compose.screen.calendar

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.GameTimeMs
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import io.mockk.every
import io.mockk.spyk
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: CalendarViewModel

    private val currentCalendar: Calendar
        get() {
            return DateUtils.getCalendar().apply {
                timeInMillis = GameTimeMs
            }
        }

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = composeViewModelProvider.getCalendarViewModel(
            dateTime = GameTimeMs,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `selectDate expects selectedDate is updated`() {
        val date = Date()
        viewModel.selectDate(date)
        assertIs(viewModel.selectedDate.value, date)
    }

    @Test
    fun `nextMonth with hasNextMonth expects numberAndDateString is updated`() {
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
    fun `nextMonth with not hasNextMonth expects numberAndDateString is not updated`() {
        val spy = spyk(viewModel) {
            every { hasNextMonth() } returns false
        }
        val expected = spy.numberAndDateString.value
        spy.nextMonth()
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `lastMonth with hasLastMonth expects numberAndDateString is updated`() {
        val spy = spyk(viewModel) {
            every { hasLastMonth() } returns true
        }
        spy.lastMonth()
        val expected = currentCalendar.let { cal ->
            cal.add(Calendar.MONTH, -1)
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            year * 100 + month to DateUtils.getDateString(year, month)
        }
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `lastMonth with not hasLastMonth expects numberAndDateString is not updated`() {
        val spy = spyk(viewModel) {
            every { hasLastMonth() } returns false
        }
        val expected = spy.numberAndDateString.value
        spy.lastMonth()
        assertIs(spy.numberAndDateString.value, expected)
    }

    @Test
    fun `clickGameCard with playedGame expects screen navigates to BoxScore`() {
        viewModel.clickGameCard(GameGenerator.getFinal())
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBoxScore::class.java
        )
    }

    @Test
    fun `clickGameCard with unPlayedGame expects screen navigates to Team`() {
        viewModel.clickGameCard(GameGenerator.getComingSoon())
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToTeam::class.java
        )
    }

    @Test
    fun `getGameCardViewModel with finalGame expects gameAndBets is correct`() {
        val expected = GameAndBetsGenerator.getFinal()
        val cardViewModel = viewModel.getGameCardViewModel(expected)
        assertIs(cardViewModel.gameAndBets, expected)
    }

    @Test
    fun `getGameCardViewModel with playingGame expects gameAndBets is correct`() {
        val expected = GameAndBetsGenerator.getPlaying()
        val cardViewModel = viewModel.getGameCardViewModel(expected)
        assertIs(cardViewModel.gameAndBets, expected)
    }

    @Test
    fun `getGameCardViewModel with comingSoonGame expects gameAndBets is correct`() {
        val expected = GameAndBetsGenerator.getComingSoon()
        val cardViewModel = viewModel.getGameCardViewModel(expected)
        assertIs(cardViewModel.gameAndBets, expected)
    }

    @Test
    fun `selectedGamesVisible with selecting today expects visible`() {
        viewModel.selectedGamesVisible.launchAndCollect()
        viewModel.selectDate(Date(GameTimeMs))
        assertIs(viewModel.selectedGamesVisible.value, true)
    }

    @Test
    fun `selectedGamesVisible with selecting next two month expects invisible`() {
        viewModel.selectedGamesVisible.launchAndCollect()
        viewModel.selectDate(Date(GameTimeMs + 24 * 60 * 60 * 1000 * 60L))
        assertIs(viewModel.selectedGamesVisible.value, false)
    }
}
