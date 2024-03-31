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
import androidx.lifecycle.SavedStateHandle
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
import com.jiachian.nbatoday.calendar.ui.CalendarScreen
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.testing.testtag.CalendarTestTag
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: CalendarViewModel

    private var navigateToGame: String? = null
    private var navigateToTeam: Int? = null
    private var navigateToBack: Boolean? = null

    @Before
    fun setup() = runTest {
        navigateToGame = null
        navigateToTeam = null
        navigateToBack = null
        viewModel = spyk(
            CalendarViewModel(
                savedStateHandle = SavedStateHandle(mapOf(MainRoute.Calendar.param to "$PlayingGameTimeMs")),
                repository = repositoryProvider.game,
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
    override fun ProvideComposable() {
        CalendarScreen(
            viewModel = viewModel,
            navigateToBoxScore = {
                navigateToGame = it
            },
            navigateToTeam = {
                navigateToTeam = it
            },
            onBack = {
                navigateToBack = true
            },
        )
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
        navigateToGame.assertIs(PlayingGameId)
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
        navigateToGame.assertIs(FinalGameId)
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
        navigateToTeam.assertIs(HomeTeamId)
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
        navigateToBack.assertIsTrue()
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
