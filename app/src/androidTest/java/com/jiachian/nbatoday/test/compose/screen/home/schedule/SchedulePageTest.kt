package com.jiachian.nbatoday.test.compose.screen.home.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePage
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.testing.testtag.ScheduleTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulePageTest : BaseAndroidTest() {
    private lateinit var viewModel: SchedulePageViewModel

    @Before
    fun setup() = runTest {
        viewModel = spyk(
            SchedulePageViewModel(
                scheduleRepository = repositoryProvider.schedule,
                gameRepository = repositoryProvider.game,
                navigationController = navigationController,
                composeViewModelProvider = composeViewModelProvider,
                dispatcherProvider = dispatcherProvider
            ).apply {
                repositoryProvider.schedule.updateSchedule()
            }
        )
    }

    @After
    fun teardown() {
        unmockkObject(viewModel)
    }

    @Composable
    override fun provideComposable(): Any {
        SchedulePage(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun schedulePage_checksPlayingDay() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithText("1/1").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
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
            performClick()
            event
                .await()
                .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
                .gameId
                .assertIs(PlayingGameId)
        }
    }

    @Test
    fun schedulePage_checksPlayingDayExpandedUI() = inCompose {
        onNodeWithText("1/1").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Collapse).apply {
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(HomePlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$HomeTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("$BasicNumber")
                }
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(AwayPlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$AwayTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("$BasicNumber")
                }
            }
        }
    }

    @Test
    fun schedulePage_checksFinalDay() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithText("12/31").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
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
            performClick()
            event
                .await()
                .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
                .gameId
                .assertIs(FinalGameId)
        }
    }

    @Test
    fun schedulePage_checksFinalDayExpandedUI() = inCompose {
        onNodeWithText("12/31").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Collapse).apply {
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(HomePlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$HomeTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("$BasicNumber")
                }
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(AwayPlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$AwayTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("$BasicNumber")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("$BasicNumber")
                }
            }
        }
    }

    @Test
    fun schedulePage_checksComingSoonDay() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithText("1/2").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
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
            onNodeWithTag(GameCardTestTag.GameDetail_GameTeamInfo_Home)
                .performClick()
            event
                .await()
                .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
                .teamId
                .assertIs(HomeTeamId)
        }
    }

    @Test
    fun schedulePage_checksComingSoonDayExpandedUI() = inCompose {
        onNodeWithText("1/2").performClick()
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_GameCard)[0].apply {
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            onNodeWithTag(GameCardTestTag.GameExpandedContent_Button_Collapse).apply {
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(HomePlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$HomeTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                }
                onNodeWithTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                        .assertTextEquals(AwayPlayerFullName)
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                        .assertTextEquals("$AwayTeamAbbr | $BasicNumber | $BasicPosition")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                    onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                        .assertTextEquals("${BasicNumber.toDouble()}")
                }
            }
        }
    }

    @Test
    fun schedulePage_clicksCalendar() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent_Button_Calendar)
            .onFirst()
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToCalendar::class.java)
            .dateTime
            .assertIs(BasicTime)
    }

    @Test
    fun schedulePage_checksRefreshingUI() = inCompose {
        onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent)
            .onFirst()
            .performTouchInput { swipeDown() }
        dataHolder
            .games
            .value
            .assertIs(
                listOf(
                    GameGenerator.getFinal(),
                    GameGenerator.getPlaying(),
                    GameGenerator.getComingSoon()
                )
            )
    }

    @Test
    fun schedulePage_checksLoadingUI() {
        every {
            viewModel.groupedGamesState
        } returns MutableStateFlow(UIState.Loading())
        inCompose {
            onAllNodesWithUnmergedTree(ScheduleTestTag.SchedulePage_LoadingScreen)
                .onFirst()
                .assertIsDisplayed()
            onAllNodesWithUnmergedTree(ScheduleTestTag.ScheduleContent)
                .onFirst()
                .assertDoesNotExist()
        }
    }
}
