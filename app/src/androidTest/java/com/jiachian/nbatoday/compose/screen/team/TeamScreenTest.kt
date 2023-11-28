package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.models.TeamStatsFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.utils.assertDialogExist
import com.jiachian.nbatoday.utils.onAllNodesWithMergedTag
import com.jiachian.nbatoday.utils.onDialog
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlin.math.pow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamScreenTest : BaseAndroidTest() {

    private lateinit var viewModel: TeamViewModel
    private lateinit var repository: TestRepository
    private var isBack = false
    private var currentState: NbaState? = null

    private val team = TeamStatsFactory.getHomeTeamStats()

    private val labels = listOf(
        PlayerLabel(40.dp, "GP", PlayerSort.GP),
        PlayerLabel(40.dp, "W", PlayerSort.W),
        PlayerLabel(40.dp, "L", PlayerSort.L),
        PlayerLabel(64.dp, "WIN%", PlayerSort.WINP),
        PlayerLabel(64.dp, "PTS", PlayerSort.PTS),
        PlayerLabel(64.dp, "FGM", PlayerSort.FGM),
        PlayerLabel(64.dp, "FGA", PlayerSort.FGA),
        PlayerLabel(64.dp, "FG%", PlayerSort.FGP),
        PlayerLabel(64.dp, "3PM", PlayerSort.PM3),
        PlayerLabel(64.dp, "3PA", PlayerSort.PA3),
        PlayerLabel(64.dp, "3P%", PlayerSort.PP3),
        PlayerLabel(64.dp, "FTM", PlayerSort.FTM),
        PlayerLabel(64.dp, "FTA", PlayerSort.FTA),
        PlayerLabel(64.dp, "FT%", PlayerSort.FTP),
        PlayerLabel(48.dp, "OREB", PlayerSort.OREB),
        PlayerLabel(48.dp, "DREB", PlayerSort.DREB),
        PlayerLabel(48.dp, "REB", PlayerSort.REB),
        PlayerLabel(48.dp, "AST", PlayerSort.AST),
        PlayerLabel(48.dp, "TOV", PlayerSort.TOV),
        PlayerLabel(48.dp, "STL", PlayerSort.STL),
        PlayerLabel(48.dp, "BLK", PlayerSort.BLK),
        PlayerLabel(48.dp, "PF", PlayerSort.PF),
        PlayerLabel(48.dp, "+/-", PlayerSort.PLUSMINUS)
    )

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = TeamViewModel(
            team = team.team,
            teamRepository = repository,
            openScreen = { currentState = it },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        repository.refreshSchedule()
        composeTestRule.setContent {
            TeamScreen(
                viewModel = viewModel,
                onBack = { isBack = true }
            )
        }
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
        isBack = false
    }

    @Test
    fun team_clickBack_checksIsBack() {
        composeTestRule
            .onNodeWithMergedTag("TeamDetailScreen_Btn_Back")
            .performClick()
        assertThat(isBack, `is`(true))
    }

    @Test
    fun team_checksTeamInfoUI() {
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Text_TeamName")
            .assertTextEquals(team.team.teamFullName)
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Text_TeamRecord")
            .assertTextEquals(
                context.getString(
                    R.string.team_rank_record,
                    team.win,
                    team.lose,
                    viewModel.teamRank.value.toRank(),
                    team.teamConference.toString()
                )
            )
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PointsRank")
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PointsRank")
            .onNodeWithTag("TeamInformation_Text_Rank")
            .assertTextEquals(viewModel.teamPointsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PointsRank")
            .onNodeWithTag("TeamInformation_Text_Average")
            .assertTextEquals(team.pointsAverage.decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_ReboundsRank")
            .onNodeWithTag("TeamInformation_Text_Rank")
            .assertTextEquals(viewModel.teamReboundsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_ReboundsRank")
            .onNodeWithTag("TeamInformation_Text_Average")
            .assertTextEquals(team.reboundsAverage.decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_AssistsRank")
            .onNodeWithTag("TeamInformation_Text_Rank")
            .assertTextEquals(viewModel.teamAssistsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_AssistsRank")
            .onNodeWithTag("TeamInformation_Text_Average")
            .assertTextEquals(team.assistsAverage.decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PlusMinusRank")
            .onNodeWithTag("TeamInformation_Text_Rank")
            .assertTextEquals(viewModel.teamPlusMinusRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PlusMinusRank")
            .onNodeWithTag("TeamInformation_Text_Average")
            .assertTextEquals(team.plusMinusAverage.decimalFormat())
    }

    @Test
    fun team_checkPlayersStatsUI() {
        composeTestRule
            .onAllNodesWithMergedTag("TeamStatsScreen_Tab")[0]
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("PlayerStatistics_LC_Players")
            .apply {
                viewModel.playersStats.value.forEachIndexed { index, player ->
                    onNodeWithTag("PlayerStatistics_Column_Player", index)
                        .onNodeWithTag("PlayerStatistics_Text_PlayerName")
                        .assertTextEquals(player.playerName)
                }
            }
        viewModel.playersStats.value.forEachIndexed { index, stats ->
            composeTestRule
                .onAllNodesWithMergedTag("PlayerStatistics_Row_PlayerStats")[index]
                .apply {
                    labels.forEachIndexed { labelIndex, label ->
                        onNodeWithTag("PlayerStatistics_Text_PlayerStats", labelIndex)
                            .assertTextEquals(
                                when (label.text) {
                                    "GP" -> stats.gamePlayed.toString()
                                    "W" -> stats.win.toString()
                                    "L" -> stats.lose.toString()
                                    "WIN%" -> stats.winPercentage.decimalFormat()
                                    "PTS" -> stats.pointsAverage.decimalFormat()
                                    "FGM" -> stats.fieldGoalsMadeAverage.decimalFormat()
                                    "FGA" -> stats.fieldGoalsAttemptedAverage.decimalFormat()
                                    "FG%" -> stats.fieldGoalsPercentage.decimalFormat()
                                    "3PM" -> stats.threePointersMadeAverage.decimalFormat()
                                    "3PA" -> stats.threePointersAttemptedAverage.decimalFormat()
                                    "3P%" -> stats.threePointersPercentage.decimalFormat()
                                    "FTM" -> stats.freeThrowsMadeAverage.decimalFormat()
                                    "FTA" -> stats.freeThrowsAttemptedAverage.decimalFormat()
                                    "FT%" -> stats.freeThrowsPercentage.decimalFormat()
                                    "OREB" -> stats.reboundsOffensiveAverage.decimalFormat()
                                    "DREB" -> stats.reboundsDefensiveAverage.decimalFormat()
                                    "REB" -> stats.reboundsTotalAverage.decimalFormat()
                                    "AST" -> stats.assistsAverage.decimalFormat()
                                    "TOV" -> stats.turnoversAverage.decimalFormat()
                                    "STL" -> stats.stealsAverage.decimalFormat()
                                    "BLK" -> stats.blocksAverage.decimalFormat()
                                    "PF" -> stats.foulsPersonalAverage.decimalFormat()
                                    "+/-" -> stats.plusMinus.toString()
                                    else -> ""
                                }
                            )
                    }
                }
        }
    }

    @Test
    fun team_checksPreviousGamesUI() {
        composeTestRule
            .onAllNodesWithMergedTag("TeamStatsScreen_Tab")[1]
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("TeamStatsScreen_GamesPage_Previous")
            .apply {
                viewModel.gamesBefore.value.forEachIndexed { index, game ->
                    onNodeWithTag("GamesPage_GameStatusCard2", index)
                        .apply {
                            performClick()
                            assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
                            onNodeWithTag("ScoreBoard_TeamInfo_Home")
                                .onNodeWithTag("TeamInfo_Text_TriCode")
                                .assertTextEquals(game.game.homeTeam.team.abbreviation)
                            onNodeWithTag("ScoreBoard_TeamInfo_Away")
                                .onNodeWithTag("TeamInfo_Text_TriCode")
                                .assertTextEquals(game.game.awayTeam.team.abbreviation)
                            onNodeWithTag("ScoreBoard_TeamInfo_Home")
                                .onNodeWithTag("TeamInfo_Text_Score")
                                .assertTextEquals(game.game.homeTeam.score.toString())
                            onNodeWithTag("ScoreBoard_TeamInfo_Away")
                                .onNodeWithTag("TeamInfo_Text_Score")
                                .assertTextEquals(game.game.awayTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_GameStatus")
                                .assertTextEquals(game.game.gameStatusFormatText)
                            onNodeWithTag("GameStatusCard2_Btn_Bet")
                                .assertDoesNotExist()
                            onNodeWithTag("ExpandContent_Btn_Expand")
                                .assertDoesNotExist()
                        }
                }
            }
    }

    @Test
    fun team_checksNextGamesUI() {
        composeTestRule
            .onAllNodesWithMergedTag("TeamStatsScreen_Tab")[2]
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("TeamStatsScreen_GamesPage_Next")
            .apply {
                viewModel.gamesAfter.value.forEachIndexed { index, game ->
                    onNodeWithTag("GamesPage_GameStatusCard2", index)
                        .apply {
                            performClick()
                            assertThat(currentState, nullValue())
                            onNodeWithTag("ScoreBoard_TeamInfo_Home")
                                .onNodeWithTag("TeamInfo_Text_TriCode")
                                .assertTextEquals(game.game.homeTeam.team.abbreviation)
                            onNodeWithTag("ScoreBoard_TeamInfo_Away")
                                .onNodeWithTag("TeamInfo_Text_TriCode")
                                .assertTextEquals(game.game.awayTeam.team.abbreviation)
                            onNodeWithTag("ScoreBoard_TeamInfo_Home")
                                .onNodeWithTag("TeamInfo_Text_Score")
                                .assertTextEquals(game.game.homeTeam.score.toString())
                            onNodeWithTag("ScoreBoard_TeamInfo_Away")
                                .onNodeWithTag("TeamInfo_Text_Score")
                                .assertTextEquals(game.game.awayTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_GameStatus")
                                .assertTextEquals(game.game.gameStatusFormatText)
                            onNodeWithTag("GameStatusCard2_Btn_Bet")
                                .assertExists()
                                .assertIsDisplayed()
                            onNodeWithTag("ExpandContent_Btn_Expand")
                                .assertDoesNotExist()
                        }
                }
            }
    }

    @Test
    fun team_betOnGame_checksUI() {
        composeTestRule
            .onAllNodesWithMergedTag("TeamStatsScreen_Tab")[2]
            .performClick()
        val card = composeTestRule
            .onNodeWithMergedTag("TeamStatsScreen_GamesPage_Next")
            .onNodeWithTag("GamesPage_GameStatusCard2", 0)
        card
            .onNodeWithTag("GameStatusCard2_Btn_Bet")
            .assertIsDisplayed()
            .performClick()
        val loginDialog = composeTestRule
            .assertDialogExist()
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog
            .onNodeWithTag("LoginDialog_AccountTextField")
            .onNodeWithTag("AccountTextField_TextField_Account")
            .performTextInput(UserAccount)
        loginDialog
            .onNodeWithTag("LoginDialog_PasswordTextField")
            .onNodeWithTag("PasswordTextField_TextFiled_Password")
            .performTextInput(UserPassword)
        loginDialog
            .onNodeWithTag("LoginDialog_Btn_Login")
            .performClick()
        val betDialog = composeTestRule
            .assertDialogExist()
            .onDialog()
            .onNodeWithTag("BetDialog_Dialog")
        betDialog
            .onNodeWithTag("BetDialogContent_BetDialogTeamEdit_Home")
            .onNodeWithTag("BetDialogTeamEdit_TextField_Bet")
            .performTextInput(UserPoints.toString())
        betDialog
            .onNodeWithTag("BetDialog_Btn_Confirm")
            .performClick()
        composeTestRule
            .onDialog()
            .onNodeWithTag("BetDialog_Alert_Warning")
            .onNodeWithTag("BetDialog_Alert_Confirm")
            .performClick()
        card
            .onNodeWithTag("GameStatusCard2_Btn_Bet")
            .assertDoesNotExist()
    }

    private fun Int.toRank(): String {
        return when (this) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${this}th"
        }
    }

    private fun Double.decimalFormat(radix: Int = 1): String {
        val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
        return value.toString()
    }
}
