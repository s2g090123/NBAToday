package com.jiachian.nbatoday.test.compose.screen.team

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.team.ui.main.TeamScreen
import com.jiachian.nbatoday.team.ui.main.TeamViewModel
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerLabel
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.testing.testtag.TeamTestTag
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.toRank
import com.jiachian.nbatoday.utils.waitUntilExists
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: TeamViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.team.addTeams()
        repositoryProvider.schedule.updateSchedule()
        viewModel = TeamViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Team.param to "$HomeTeamId")),
            teamUseCase = useCaseProvider.team,
            gameUseCase = useCaseProvider.game,
            getUser = useCaseProvider.user.getUser,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            TeamScreen(
                state = viewModel.state,
                colors = viewModel.colors,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun checksTeamInfo() {
        val team = TeamGenerator.getHome()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(TeamTestTag.TeamNameAndStanding_Text_TeamName))
            onNodeWithUnmergedTree(TeamTestTag.TeamNameAndStanding_Text_TeamName)
                .assertTextEquals(team.team.teamFullName)
            onNodeWithUnmergedTree(TeamTestTag.TeamNameAndStanding_Text_StandingDetail)
                .assertTextEquals(team.getStandingDetail(1))
            onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Points).let {
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                    .assertTextEquals(1.toRank())
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                    .assertTextEquals("${team.pointsAverage}")
            }
            onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Rebounds).let {
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                    .assertTextEquals(1.toRank())
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                    .assertTextEquals("${team.reboundsTotalAverage}")
            }
            onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_Assists).let {
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                    .assertTextEquals(1.toRank())
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                    .assertTextEquals("${team.assistsAverage}")
            }
            onNodeWithUnmergedTree(TeamTestTag.TeamStatsDetail_TeamRankBox_PlusMinus).let {
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Rank)
                    .assertTextEquals(1.toRank())
                it.onNodeWithTag(TeamTestTag.TeamRankBox_Text_Average)
                    .assertTextEquals("${team.plusMinus}")
            }
        }
    }

    @Test
    fun checksPlayerStats() {
        val player = TeamPlayerGenerator.getHome()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(TeamTestTag.TeamTab))
            onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[0]
                .performClick()
            onNodeWithUnmergedTree(TeamTestTag.TeamPlayerRow_Text_PlayerName)
                .assertTextEquals(player.playerName)
            TeamPlayerLabel.values().forEachIndexed { index, label ->
                val value = LabelHelper.getValueByLabel(label, player)
                onAllNodesWithUnmergedTree(TeamTestTag.TeamPlayerStatsText)[index]
                    .assertTextEquals(value)
            }
        }
    }

    @Test
    fun checksPreviousGames() = runTest {
        val game = GameGenerator.getFinal()
        composeTestRule.let {
            it.waitUntilExists(hasTestTag(TeamTestTag.TeamTab))
            it.onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[1]
                .performClick()
            it.onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[0].apply {
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
            }
        }
    }

    @Test
    fun checksNextGames() = runTest {
        composeTestRule.let {
            it.waitUntilExists(hasTestTag(TeamTestTag.TeamTab))
            it.onAllNodesWithUnmergedTree(TeamTestTag.TeamTab)[2]
                .performClick()
            it.onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[0].apply {
                val game = GameGenerator.getPlaying()
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
            }
            it.onAllNodesWithUnmergedTree(TeamTestTag.TeamGamePage_GameCard)[1].apply {
                val game = GameGenerator.getComingSoon()
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
                    .assertIsDisplayed()
                    .performClick()
                navigationController.showLoginDialog.assertIsTrue()
                useCaseProvider.user.userLogin(UserAccount, UserPassword)
                onNodeWithTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                    .performClick()
                navigationController.showBetDialog.assertIs(ComingSoonGameId)
            }
        }
    }

    @Test
    fun back_moveToBack() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(TeamTestTag.TeamScreen_Button_Back)
                .performClick()
            navigationController.back.assertIsTrue()
        }
    }
}
