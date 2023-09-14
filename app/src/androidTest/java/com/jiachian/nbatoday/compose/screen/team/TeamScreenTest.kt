package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.TeamStatsFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.utils.assertDialogExist
import com.jiachian.nbatoday.utils.onAllNodesWithMergedTag
import com.jiachian.nbatoday.utils.onDialog
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlin.math.pow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
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

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = TeamViewModel(
            team = team.team,
            repository = repository,
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
            .printToLog("HAHA")
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PointsRank")
            .onNodeWithTag("TeamInformation_Text_PointsRank")
            .assertTextEquals(viewModel.teamPointsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PointsRank")
            .onNodeWithTag("TeamInformation_Text_Points")
            .assertTextEquals((team.points.toDouble() / team.gamePlayed).decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_ReboundsRank")
            .onNodeWithTag("TeamInformation_Text_ReboundsRank")
            .assertTextEquals(viewModel.teamReboundsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_ReboundsRank")
            .onNodeWithTag("TeamInformation_Text_Rebounds")
            .assertTextEquals((team.reboundsTotal.toDouble() / team.gamePlayed).decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_AssistsRank")
            .onNodeWithTag("TeamInformation_Text_AssistsRank")
            .assertTextEquals(viewModel.teamAssistsRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_AssistsRank")
            .onNodeWithTag("TeamInformation_Text_Assists")
            .assertTextEquals((team.assists.toDouble() / team.gamePlayed).decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PlusMinusRank")
            .onNodeWithTag("TeamInformation_Text_PlusMinusRank")
            .assertTextEquals(viewModel.teamPlusMinusRank.value.toRank())
        composeTestRule
            .onNodeWithMergedTag("TeamInformation_Column_PlusMinusRank")
            .onNodeWithTag("TeamInformation_Text_PlusMinus")
            .assertTextEquals(team.plusMinus.toDouble().toString())
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
                    viewModel.playerLabels.value.forEachIndexed { labelIndex, label ->
                        onNodeWithTag("PlayerStatistics_Text_PlayerStats", labelIndex)
                            .assertTextEquals(
                                when (label.text) {
                                    "GP" -> stats.gamePlayed.toString()
                                    "W" -> stats.win.toString()
                                    "L" -> stats.lose.toString()
                                    "WIN%" -> stats.winPercentage.decimalFormat()
                                    "PTS" -> (stats.points.toDouble() / stats.gamePlayed).decimalFormat()
                                    "FGM" -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                    "FGA" -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                    "FG%" -> stats.fieldGoalsPercentage.decimalFormat()
                                    "3PM" -> (stats.threePointersMade.toDouble() / stats.gamePlayed).decimalFormat()
                                    "3PA" -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                    "3P%" -> stats.threePointersPercentage.decimalFormat()
                                    "FTM" -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).decimalFormat()
                                    "FTA" -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                                    "FT%" -> stats.freeThrowsPercentage.decimalFormat()
                                    "OREB" -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).decimalFormat()
                                    "DREB" -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).decimalFormat()
                                    "REB" -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).decimalFormat()
                                    "AST" -> (stats.assists.toDouble() / stats.gamePlayed).decimalFormat()
                                    "TOV" -> (stats.turnovers.toDouble() / stats.gamePlayed).decimalFormat()
                                    "STL" -> (stats.steals.toDouble() / stats.gamePlayed).decimalFormat()
                                    "BLK" -> (stats.blocks.toDouble() / stats.gamePlayed).decimalFormat()
                                    "PF" -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).decimalFormat()
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
                            onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                                .assertTextEquals(game.game.homeTeam.team.abbreviation)
                            onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                                .assertTextEquals(game.game.awayTeam.team.abbreviation)
                            onNodeWithTag("GameStatusCard2_Text_HomeScore")
                                .assertTextEquals(game.game.homeTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_AwayScore")
                                .assertTextEquals(game.game.awayTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_GameStatus")
                                .assertTextEquals(game.game.gameStatusText)
                            onNodeWithTag("GameStatusCard2_Btn_Bet")
                                .assertDoesNotExist()
                            onNodeWithTag("GameStatusCard2_Btn_Expand")
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
                            onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                                .assertTextEquals(game.game.homeTeam.team.abbreviation)
                            onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                                .assertTextEquals(game.game.awayTeam.team.abbreviation)
                            onNodeWithTag("GameStatusCard2_Text_HomeScore")
                                .assertTextEquals(game.game.homeTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_AwayScore")
                                .assertTextEquals(game.game.awayTeam.score.toString())
                            onNodeWithTag("GameStatusCard2_Text_GameStatus")
                                .assertTextEquals(
                                    game.game.gameStatusText.replaceFirst(" ", "\n").trim()
                                )
                            onNodeWithTag("GameStatusCard2_Btn_Bet")
                                .assertExists()
                                .assertIsDisplayed()
                            onNodeWithTag("GameStatusCard2_Btn_Expand")
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
            .onNodeWithTag("LoginDialog_EditText_Account")
            .performTextInput(USER_ACCOUNT)
        loginDialog
            .onNodeWithTag("LoginDialog_EditText_Password")
            .performTextInput(USER_PASSWORD)
        loginDialog
            .onNodeWithTag("LoginDialog_Btn_Login")
            .performClick()
        val betDialog = composeTestRule
            .assertDialogExist()
            .onDialog()
            .onNodeWithTag("BetDialog_Dialog")
        betDialog
            .onNodeWithTag("BetDialog_EditText_HomeBet")
            .performTextInput(USER_POINTS.toString())
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
