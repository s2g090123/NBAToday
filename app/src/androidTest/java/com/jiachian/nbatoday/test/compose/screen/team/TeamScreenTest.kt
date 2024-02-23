package com.jiachian.nbatoday.test.compose.screen.team

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GamePlayed
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamLocation
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.screen.team.TeamScreen
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.toRank
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: TeamViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.team.insertTeams()
        repositoryProvider.schedule.updateSchedule()
        viewModel = spyk(
            TeamViewModel(
                teamId = HomeTeamId,
                teamRepository = repositoryProvider.team,
                gameRepository = repositoryProvider.game,
                navigationController = navigationController,
                composeViewModelProvider = composeViewModelProvider,
                dispatcherProvider = dispatcherProvider,
            )
        )
    }

    @Composable
    override fun provideComposable(): Any {
        TeamScreen(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun teamScreen_checksInfoUI() = inCompose {
        onNodeWithUnmergedTree(TeamTestTag.TeamNameAndStanding_Text_TeamName)
            .assertTextEquals("$HomeTeamLocation $HomeTeamName")
        onNodeWithUnmergedTree(TeamTestTag.TeamNameAndStanding_Text_StandingDetail)
            .assertTextEquals("$BasicNumber - $BasicNumber | ${1.toRank()} in ${NBATeam.Conference.EAST}")
        onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Points).apply {
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                .assertTextEquals(1.toRank())
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                .assertTextEquals("${(BasicNumber / GamePlayed.toDouble()).decimalFormat()}")
        }
        onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Rebounds).apply {
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                .assertTextEquals(1.toRank())
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                .assertTextEquals("${(BasicNumber / GamePlayed.toDouble()).decimalFormat()}")
        }
        onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Assists).apply {
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                .assertTextEquals(1.toRank())
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                .assertTextEquals("${(BasicNumber / GamePlayed.toDouble()).decimalFormat()}")
        }
        onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_PlusMinus).apply {
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                .assertTextEquals(1.toRank())
            onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                .assertTextEquals("${BasicNumber.toDouble().decimalFormat()}")
        }
    }

    @Test
    fun teamScreen_checksPlayersUI() = inCompose {
        onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[0]
            .performClick()
        onNodeWithUnmergedTree(TeamTestTag.TeamPlayerRow_Text_PlayerName)
            .assertTextEquals(HomePlayerFullName)
        TeamPlayerLabel.values().forEachIndexed { index, label ->
            val value = LabelHelper.getValueByLabel(label, TeamPlayerGenerator.getHome())
            onAllNodesWithUnmergedTree(TeamTestTag.TeamPlayerStatsText)[index]
                .assertTextEquals(value)
        }
    }

    @Test
    fun teamScreen_checksPreviousGamesUI() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[1]
            .performClick()
        onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[0].apply {
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
                .assertTextEquals(GameStatusFinal)
            onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
            performClick()
        }
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            .gameId
            .assertIs(FinalGameId)
    }

    @Test
    fun teamScreen_checksNextGamesUI() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[2]
            .performClick()
        onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[0].apply {
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
        }
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            .gameId
            .assertIs(PlayingGameId)
        onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[1].apply {
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
    }

    @Test
    fun teamScreen_clicksBack() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithUnmergedTree(TeamTestTag.TeamScreen_Button_Back)
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            .departure
            .assertIs(MainRoute.Team)
    }

    @Test
    fun teamScreen_checksLoading() {
        every {
            viewModel.teamUIState
        } returns MutableStateFlow(UIState.Loading())
        inCompose {
            onNodeWithUnmergedTree(TeamTestTag.TeamScreen_LoadingScreen)
                .assertIsDisplayed()
        }
    }
}
