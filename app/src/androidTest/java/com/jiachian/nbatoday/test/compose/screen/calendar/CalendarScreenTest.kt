package com.jiachian.nbatoday.test.compose.screen.calendar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.calendar.ui.CalendarScreen
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.testing.testtag.CalendarTestTag
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = CalendarViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Calendar.param to "$PlayingGameTimeMs")),
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            CalendarScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun calendarScreen_checkCurrentMonth_withPlayingGame() {
        val game = GameGenerator.getPlaying()
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarGameCard)
                .performClick()
            navigationController.toBoxScore.assertIs(game.gameId)
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
                .assertIsEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
                .assertIsEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
                .assertTextEquals(DateUtils.getDateString(2023, 0))
        }
    }

    @Test
    fun calendarScreen_checkNextMonth_withComingSoonGame() {
        val game = GameGenerator.getComingSoon()
        composeTestRule.let {
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
                .performClick()
            it.onAllNodesWithUnmergedTree(CalendarTestTag.DateBox_Text_Date)
                .filter(hasText("1"))
                .onFirst()
                .performClick()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
                .performClick()
            navigationController.showLoginDialog.assertIsTrue()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home)
                .performClick()
            navigationController.toTeam.assertIs(game.homeTeam.team.teamId)
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
                .assertIsEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
                .assertIsNotEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
                .assertTextEquals(DateUtils.getDateString(2023, 1))
        }
    }

    @Test
    fun calendarScreen_checkPreviousMonth_withFinalGame() {
        val game = GameGenerator.getFinal()
        composeTestRule.let {
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
                .performClick()
            it.onAllNodesWithUnmergedTree(CalendarTestTag.DateBox_Text_Date)
                .filter(hasText("31"))
                .onFirst()
                .performClick()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarGameCard)
                .performClick()
            navigationController.toBoxScore.assertIs(game.gameId)
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Last)
                .assertIsNotEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_CalendarArrowButton_Next)
                .assertIsEnabled()
            it.onNodeWithUnmergedTree(CalendarTestTag.CalendarNavigationBar_Text_Date)
                .assertTextEquals(DateUtils.getDateString(2022, 11))
        }
    }

    @Test
    fun calendarScreen_close() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(CalendarTestTag.CalendarTopBar_Btn_Close)
                .performClick()
            navigationController.back.assertIsTrue()
        }
    }
}
