package com.jiachian.nbatoday.test.compose.screen.home.schedule

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeDown
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.home.schedule.ui.SchedulePage
import com.jiachian.nbatoday.home.schedule.ui.SchedulePageViewModel
import com.jiachian.nbatoday.home.schedule.ui.model.DateData
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.testing.testtag.ScheduleTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulePageTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: SchedulePageViewModel

    @Before
    fun setup() {
        viewModel = SchedulePageViewModel(
            scheduleUseCase = useCaseProvider.schedule,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            SchedulePage(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun checkTodaySchedule_playingGame() = runTest {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getPlaying()
        composeTestRule.let {
            it.onNodeWithText("1/1").performClick()
            it.onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
                onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                        .assertTextEquals(game.homeTeam.team.abbreviation)
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                        .assertTextEquals("${game.homeTeam.score}")
                }
                onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                        .assertTextEquals(game.awayTeam.team.abbreviation)
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                        .assertTextEquals("${game.awayTeam.score}")
                }
                onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                    .assertTextEquals(game.statusFormattedText)
                onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                    .assertDoesNotExist()
                performClick()
                navigationController.toBoxScore.assertIs(PlayingGameId)
            }
        }
    }

    @Test
    fun checkYesterdaySchedule_finalGame() = runTest {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getFinal()
        composeTestRule.let {
            it.onNodeWithText("12/31").performClick()
            it.onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
                onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                        .assertTextEquals(game.homeTeam.team.abbreviation)
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                        .assertTextEquals("${game.homeTeam.score}")
                }
                onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                        .assertTextEquals(game.awayTeam.team.abbreviation)
                    onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                        .assertTextEquals("${game.awayTeam.score}")
                }
                onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                    .assertTextEquals(game.statusFormattedText)
                onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                    .assertDoesNotExist()
                performClick()
                navigationController.toBoxScore.assertIs(FinalGameId)
            }
        }
    }

    @Test
    fun swipeRefresh_scheduleIsUpdated() {
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent)
                .onFirst()
                .performTouchInput { swipeDown() }
            onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)
                .onFirst()
        }
    }

    @Test
    fun clickCalendar_moveToCalendar() {
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_Button_Calendar)
                .onFirst()
                .performClick()
            navigationController.toCalendar.assertIs(DateData(2023, 1, 1).dateString)
        }
    }
}
