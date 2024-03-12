package com.jiachian.nbatoday.test.compose.screen.home.schedule

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.models.local.game.toGameCardUIDataList
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulePageViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: SchedulePageViewModel

    @Before
    fun setup() = runTest {
        get<ScheduleRepository>().updateSchedule()
        viewModel = SchedulePageViewModel(
            scheduleRepository = get(),
            gameRepository = get(),
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `dataData expects correct`() {
        val expected = DateUtils.getCalendar().run {
            add(Calendar.DAY_OF_MONTH, -ScheduleDateRange)
            List(ScheduleDateRange * 2 + 1) {
                DateData(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                ).also {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }
        assertIs(viewModel.dateData, expected)
    }

    @Test
    fun `groupedGamesState expects correct`() {
        assertIsA(
            viewModel.groupedGamesState.value,
            UIState.Loading::class.java
        )
        viewModel.groupedGamesState.launchAndCollect()
        val gamesAndBets = dataHolder.gamesAndBets.stateIn(emptyList())
        assertIsA(
            viewModel.groupedGamesState.value,
            UIState.Loaded::class.java
        )
        val dateData = viewModel.selectedDate
        val dateDataToGames = viewModel.groupedGamesState.value.getDataOrNull()
        val actual = dateDataToGames?.get(dateData)
        val expected = gamesAndBets.value.filter {
            it.game.gameDateTime.time == PlayingGameTimeMs
        }.toGameCardUIDataList()
        assertIs(actual, expected)
    }

    @Test
    fun `selectDate() expects selectedDate is updated`() {
        val dateData = DateUtils.getCalendar().run {
            add(Calendar.DAY_OF_MONTH, 1)
            DateData(
                year = get(Calendar.YEAR),
                month = get(Calendar.MONTH) + 1,
                day = get(Calendar.DAY_OF_MONTH)
            )
        }
        viewModel.selectDate(dateData)
        assertIs(viewModel.selectedDate, dateData)
    }

    @Test
    fun `updateSelectedSchedule() expects groupedGamesState is updated`() = launch {
        viewModel.updateSelectedSchedule()
        advanceUntilIdle()
        assertIsA(
            viewModel.groupedGamesState.value,
            UIState.Loading::class.java
        )
        viewModel.groupedGamesState.launchAndCollect()
        val gamesAndBets = dataHolder.gamesAndBets.stateIn(emptyList())
        assertIsA(
            viewModel.groupedGamesState.value,
            UIState.Loaded::class.java
        )
        val dateData = viewModel.selectedDate
        val dateDataToGames = viewModel.groupedGamesState.value.getDataOrNull()
        val actual = dateDataToGames?.get(dateData)
        val expected = gamesAndBets.value.filter {
            it.game.gameDateTime.time == PlayingGameTimeMs
        }.toGameCardUIDataList()
        assertIs(actual, expected)
    }
}
