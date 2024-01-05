package com.jiachian.nbatoday.test.compose.screen.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.compose.screen.calendar.CalendarScreen
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.CalendarTestTag
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() = runTest {
        viewModel = spyk(
            CalendarViewModel(
                dateTime = PlayingGameTimeMs,
                repository = repositoryProvider.game,
                navigationController = navigationController,
                composeViewModelProvider = composeViewModelProvider,
                dispatcherProvider = dispatcherProvider,
            )
        ).apply {
            repositoryProvider.schedule.updateSchedule()
            insertNextMonthGame()
        }
    }

    @After
    fun teardown() {
        unmockkObject(viewModel)
    }

    @Composable
    override fun provideComposable(): Any {
        CalendarScreen(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun calendarScreen_checksUI() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
            .assertIsEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
            .assertIsEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
            .assertTextEquals(DateUtils.getDateString(2023, 0))
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0]
            .assertIsDisplayed()
    }

    @Test
    fun calendarScreen_previousMonth_checksUI() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
            .performClick()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
            .assertIsNotEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
            .assertIsEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
            .assertTextEquals(DateUtils.getDateString(2022, 11))
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)
            .assertCountEquals(0)
    }

    @Test
    fun calendarScreen_nextMonth_checksUI() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
            .performClick()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
            .assertIsEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
            .assertIsNotEnabled()
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
            .assertTextEquals(DateUtils.getDateString(2023, 1))
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)
            .assertCountEquals(0)
    }

    @Test
    fun calendarScreen_checksPlayingGameCard() = inCompose {
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(HomeTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(AwayTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(GameStatusPrepare.replaceFirst(" ", "\n").trim())
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
        }
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0]
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            ?.gameId
            .assertIs(PlayingGameId)
    }

    @Test
    fun calendarScreen_checksFinalGameCard() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
            .performClick()
        onAllNodesWithUnmergedTree(CalendarTestTag.DateBox_Text_Date)
            .filter(hasText("31"))
            .onFirst().performClick()
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(HomeTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(AwayTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(GameStatusFinal.replaceFirst(" ", "\n").trim())
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
        }
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0]
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            ?.gameId
            .assertIs(FinalGameId)
    }

    @Test
    fun calendarScreen_checksComingSoonGameCard() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
            .performClick()
        onAllNodesWithUnmergedTree(CalendarTestTag.DateBox_Text_Date)
            .filter(hasText("1"))
            .onFirst().performClick()
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(HomeTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(AwayTeamAbbr)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("$BasicNumber")
            }
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(GameStatusPrepare.replaceFirst(" ", "\n").trim())
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
        }
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)[0]
            .onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home)
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
            ?.teamId
            .assertIs(HomeTeamId)
    }

    @Test
    fun calendarScreen_clickAfterTomorrow_selectedGamesDisappear() = inCompose {
        onAllNodesWithUnmergedTree(CalendarTestTag.DateBox)[2]
            .performClick()
        onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)
            .assertCountEquals(0)
    }

    @Test
    fun calendarScreen_checksCalendarLoading() {
        every {
            viewModel.calendarDatesState
        } returns MutableStateFlow(UIState.Loading())
        inCompose {
            onNodeWithUnmergedTree(CalendarTestTag.CalendarContent_LoadingScreen_Calendar)
                .assertIsDisplayed()
            onAllNodesWithUnmergedTree(CalendarTestTag.DateBox)
                .assertCountEquals(0)
        }
    }

    @Test
    fun calendarScreen_checksGamesLoading() {
        every {
            viewModel.loadingGames
        } returns MutableStateFlow(true)
        inCompose {
            onNodeWithUnmergedTree(CalendarTestTag.CalendarContent_LoadingScreen_Games)
                .assertIsDisplayed()
            onAllNodesWithUnmergedTree(CalendarTestTag.CalendarGameCard)
                .assertCountEquals(0)
        }
    }

    @Test
    fun calendarScreen_closes_expectsBackScreen() = inCompose {
        onNodeWithUnmergedTree(CalendarTestTag.CalendarTopBar_Btn_Close)
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            ?.departure
            .assertIs(MainRoute.Calendar)
    }

    private fun insertNextMonthGame() {
        dataHolder.games.value = dataHolder.games.value.toMutableList().apply {
            add(
                GameGenerator.getComingSoon().copy(
                    gameId = "3",
                    gameDate = Date(1675270800000L),
                    gameDateTime = Date(1675270800000L)
                )
            )
        }
    }
}
