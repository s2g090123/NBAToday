package com.jiachian.nbatoday.test.compose.screen.home.schedule

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulePageViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: SchedulePageViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = composeViewModelProvider.getSchedulePageViewModel(
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
        val dateData = viewModel.getSelectedDate()
        val dateDataToGames = viewModel.groupedGamesState.value.getDataOrNull()
        val games = dateDataToGames?.get(dateData)
        assertIs(games, gamesAndBets.value)
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
        assertIs(viewModel.getSelectedDate(), dateData)
    }

    @Test
    fun `updateSelectedSchedule() expects groupedGamesState is updated`() = runTest {
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
        val dateData = viewModel.getSelectedDate()
        val dateDataToGames = viewModel.groupedGamesState.value.getDataOrNull()
        val games = dateDataToGames?.get(dateData)
        assertIs(games, gamesAndBets.value)
    }

    @Test
    fun `onClickGame(finalGame) expects screen navigates to BoxScore`() {
        viewModel.onClickGame(GameAndBetsGenerator.getFinal())
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBoxScore::class.java
        )
    }

    @Test
    fun `onClickGame(playingGame) expects screen navigates to BoxScore`() {
        viewModel.onClickGame(GameAndBetsGenerator.getPlaying())
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBoxScore::class.java
        )
    }

    @Test
    fun `onClickGame(comingSoonGame) expects screen navigates to Team`() {
        viewModel.onClickGame(GameAndBetsGenerator.getComingSoon())
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToTeam::class.java
        )
    }

    @Test
    fun `onClickCalendar() expects screen navigates to Calendar`() {
        viewModel.onClickCalendar()
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToCalendar::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToCalendar
        assertIs(
            event?.dateTime,
            BasicTime,
        )
    }

    @Test
    fun `getGameCardViewModel(finalGame) expects gameAndBets is correct`() {
        val gameAndBets = GameAndBetsGenerator.getFinal()
        val cardViewModel = viewModel.getGameCardViewModel(gameAndBets)
        assertIs(cardViewModel.gameAndBets, gameAndBets)
    }

    @Test
    fun `getGameCardViewModel(playingGame) expects gameAndBets is correct`() {
        val gameAndBets = GameAndBetsGenerator.getPlaying()
        val cardViewModel = viewModel.getGameCardViewModel(gameAndBets)
        assertIs(cardViewModel.gameAndBets, gameAndBets)
    }

    @Test
    fun `getGameCardViewModel(comingSoonGame) expects gameAndBets is correct`() {
        val gameAndBets = GameAndBetsGenerator.getComingSoon()
        val cardViewModel = viewModel.getGameCardViewModel(gameAndBets)
        assertIs(cardViewModel.gameAndBets, gameAndBets)
    }
}
