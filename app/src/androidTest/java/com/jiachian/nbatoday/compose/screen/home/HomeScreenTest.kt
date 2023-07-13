package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jiachian.nbatoday.BASIC_TIME
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.GAME_CODE
import com.jiachian.nbatoday.GAME_STATUS_FINAL
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_NAME
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.GameTeamFactory
import com.jiachian.nbatoday.data.TeamStatsFactory
import com.jiachian.nbatoday.data.TestDataStore
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.utils.assertDialogDoesNotExist
import com.jiachian.nbatoday.utils.assertDialogExist
import com.jiachian.nbatoday.utils.getOrAssert
import com.jiachian.nbatoday.utils.onAllNodesWithMergedTag
import com.jiachian.nbatoday.utils.onDialog
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.math.pow

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest : BaseAndroidTest() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: TestRepository
    private lateinit var dataStore: TestDataStore
    private var currentState: NbaState? = null

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        dataStore = TestDataStore()
        viewModel = HomeViewModel(
            repository = repository,
            dataStore = dataStore,
            openScreen = { currentState = it },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        repository.refreshSchedule()
        repository.insertGame(getYesterdayGame())
        composeTestRule.setContent {
            HomeScreen(viewModel = viewModel)
        }
    }

    @After
    fun teardown() {
        repository.clear()
        dataStore.clear()
        currentState = null
    }

    @Test
    fun homeSchedule_clickCalendar_checksCurrentState() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Schedule")
            .performClick()
        val pageRoot = composeTestRule
            .onNodeWithMergedTag("SchedulePage_Pager")
        pageRoot
            .onNodeWithTag("SchedulePage_Box")
            .onNodeWithTag("SchedulePage_LZ_Body")
            .onNodeWithTag("SchedulePage_Btn_Calendar")
            .performClick()
        assertThat(currentState, instanceOf(NbaState.Calendar::class.java))
    }

    @Test
    fun homeSchedule_checksToday() {
        val dateData = DateData(2023, 1, 1)
        val todayGames = viewModel.scheduleGames.value[dateData].getOrAssert()
        assertThat(todayGames.size, equalTo(2))
        val firstGame = todayGames[0]
        val secondGame = todayGames[1]
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Schedule")
            .performClick()
        composeTestRule
            .onNodeWithText("1/1")
            .performClick()
        val pageRoot = composeTestRule
            .onNodeWithMergedTag("SchedulePage_Pager")
        val bodyRoot = pageRoot
            .onNodeWithTag("SchedulePage_Box")
            .onNodeWithTag("SchedulePage_LZ_Body")
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .performClick()
            .apply {
                assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
                onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                    .assertTextEquals(firstGame.game.homeTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                    .assertTextEquals(firstGame.game.awayTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_HomeScore")
                    .assertTextEquals(firstGame.game.homeTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_AwayScore")
                    .assertTextEquals(firstGame.game.awayTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_GameStatus")
                    .assertTextEquals(firstGame.game.gameStatusText)
                onNodeWithTag("GameStatusCard2_Btn_Bet")
                    .assertDoesNotExist()
                onNodeWithTag("GameStatusCard2_Btn_Expand")
                    .assertIsDisplayed()
                    .performClick()
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .assertIsDisplayed()
                    .performClick()

                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 0)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.homeLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 1)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.awayLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
            }
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 1)
            .performClick()
            .apply {
                assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
                onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                    .assertTextEquals(secondGame.game.homeTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                    .assertTextEquals(secondGame.game.awayTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_HomeScore")
                    .assertTextEquals(secondGame.game.homeTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_AwayScore")
                    .assertTextEquals(secondGame.game.awayTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_GameStatus")
                    .assertTextEquals(secondGame.game.gameStatusText)
                onNodeWithTag("GameStatusCard2_Btn_Bet")
                    .assertDoesNotExist()
                onNodeWithTag("GameStatusCard2_Btn_Expand")
                    .assertIsDisplayed()
                    .performClick()
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .assertIsDisplayed()
                    .performClick()

                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 0)
                    .apply {
                        val pointsLeader = secondGame.game.gameLeaders?.homeLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 1)
                    .apply {
                        val pointsLeader = secondGame.game.gameLeaders?.awayLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
            }
    }

    @Test
    fun homeSchedule_checksTomorrow() {
        val dateData = DateData(2023, 1, 2)
        val todayGames = viewModel.scheduleGames.value[dateData].getOrAssert()
        assertThat(todayGames.size, equalTo(1))
        val firstGame = todayGames[0]
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Schedule")
            .performClick()
        composeTestRule
            .onNodeWithText("1/2")
            .performClick()
        val pageRoot = composeTestRule
            .onNodeWithMergedTag("SchedulePage_Pager")
        val bodyRoot = pageRoot
            .onNodeWithTag("SchedulePage_Box")
            .onNodeWithTag("SchedulePage_LZ_Body")
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .performClick()
            .apply {
                assertThat(currentState, instanceOf(NbaState.Team::class.java))
                onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                    .assertTextEquals(firstGame.game.homeTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                    .assertTextEquals(firstGame.game.awayTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_HomeScore")
                    .assertTextEquals(firstGame.game.homeTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_AwayScore")
                    .assertTextEquals(firstGame.game.awayTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_GameStatus")
                    .assertTextEquals(firstGame.game.gameStatusText.replaceFirst(" ", "\n"))
                onNodeWithTag("GameStatusCard2_Btn_Bet")
                    .assertIsDisplayed()
                onNodeWithTag("GameStatusCard2_Btn_Expand")
                    .assertIsDisplayed()
                    .performClick()
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .assertIsDisplayed()
                    .performClick()

                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 0)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.homeLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toString())
                    }
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 1)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.awayLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toString())
                    }
            }
    }

    @Test
    fun homeSchedule_checksYesterday() {
        val dateData = DateData(2022, 12, 31)
        val todayGames = viewModel.scheduleGames.value[dateData].getOrAssert()
        assertThat(todayGames.size, equalTo(1))
        val firstGame = todayGames[0]
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Schedule")
            .performClick()
        composeTestRule
            .onNodeWithText("12/31")
            .performClick()
        val pageRoot = composeTestRule
            .onNodeWithMergedTag("SchedulePage_Pager")
        val bodyRoot = pageRoot
            .onNodeWithTag("SchedulePage_Box")
            .onNodeWithTag("SchedulePage_LZ_Body")
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .performClick()
            .apply {
                assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
                onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
                    .assertTextEquals(firstGame.game.homeTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
                    .assertTextEquals(firstGame.game.awayTeam.teamTricode)
                onNodeWithTag("GameStatusCard2_Text_HomeScore")
                    .assertTextEquals(firstGame.game.homeTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_AwayScore")
                    .assertTextEquals(firstGame.game.awayTeam.score.toString())
                onNodeWithTag("GameStatusCard2_Text_GameStatus")
                    .assertTextEquals(firstGame.game.gameStatusText)
                onNodeWithTag("GameStatusCard2_Btn_Bet")
                    .assertDoesNotExist()
                onNodeWithTag("GameStatusCard2_Btn_Expand")
                    .assertIsDisplayed()
                    .performClick()
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .assertIsDisplayed()
                    .performClick()

                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 0)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.homeLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
                onNodeWithTag("GameStatusCard2_Btn_Collapse")
                    .onNodeWithTag("GameStatusCard2_LeaderInfo")
                    .onNodeWithTag("LeaderInfo_Info")
                    .onNodeWithTag("LeaderInfo_Leaders", 1)
                    .apply {
                        val pointsLeader = firstGame.game.gameLeaders?.awayLeaders.getOrAssert()
                        onNodeWithTag("LeaderInfo_Text_PlayerInfo")
                            .assertTextEquals(
                                context.getString(
                                    R.string.player_info,
                                    pointsLeader.teamTricode,
                                    pointsLeader.jerseyNum,
                                    pointsLeader.position
                                )
                            )
                        onNodeWithTag("LeaderInfo_Text_Ast")
                            .assertTextEquals(pointsLeader.assists.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Reb")
                            .assertTextEquals(pointsLeader.rebounds.toInt().toString())
                        onNodeWithTag("LeaderInfo_Text_Pts")
                            .assertTextEquals(pointsLeader.points.toInt().toString())
                    }
            }
    }

    @Test
    fun homeSchedule_clickBetBtn_checksUI() {
        val dateData = DateData(2023, 1, 2)
        val todayGames = viewModel.scheduleGames.value[dateData].getOrAssert()
        assertThat(todayGames.size, equalTo(1))
        val firstGame = todayGames[0]
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Schedule")
            .performClick()
        composeTestRule
            .onNodeWithText("1/2")
            .performClick()
        val pageRoot = composeTestRule
            .onNodeWithMergedTag("SchedulePage_Pager")
        val bodyRoot = pageRoot
            .onNodeWithTag("SchedulePage_Box")
            .onNodeWithTag("SchedulePage_LZ_Body")
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .onNodeWithTag("GameStatusCard2_Btn_Bet")
            .assertIsDisplayed()
            .performClick()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Login")
                .performClick()
        }
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .onNodeWithTag("GameStatusCard2_Btn_Bet")
            .assertIsDisplayed()
            .performClick()
        val betDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("BetDialog_Dialog")
        betDialog.apply {
            onNodeWithTag("BetDialog_Text_HomeRecord")
                .assertTextEquals(
                    context.getString(
                        R.string.bet_win_lose_record,
                        firstGame.game.homeTeam.wins,
                        firstGame.game.homeTeam.losses
                    )
                )
            onNodeWithTag("BetDialog_Text_AwayRecord")
                .assertTextEquals(
                    context.getString(
                        R.string.bet_win_lose_record,
                        firstGame.game.awayTeam.wins,
                        firstGame.game.awayTeam.losses
                    )
                )
            onNodeWithTag("BetDialog_EditText_HomeBet")
                .performTextInput("500")
            onNodeWithTag("BetDialog_Text_Remainder")
                .assertTextEquals(context.getString(R.string.bet_remain, USER_POINTS - 500))
            onNodeWithTag("BetDialog_EditText_AwayBet")
                .performTextInput("500")
            onNodeWithTag("BetDialog_Text_Remainder")
                .assertTextEquals(context.getString(R.string.bet_remain, USER_POINTS - 1000))
            onNodeWithTag("BetDialog_Btn_Confirm")
                .performClick()
        }
        val warningDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("BetDialog_Alert_Warning")
        warningDialog
            .onNodeWithTag("BetDialog_Alert_Confirm")
            .performClick()
        composeTestRule.assertDialogDoesNotExist()
        bodyRoot
            .onNodeWithTag("SchedulePage_GameStatusCard2", 0)
            .onNodeWithTag("GameStatusCard2_Btn_Bet")
            .assertDoesNotExist()
    }

    @Test
    fun homeStanding_checksEastUI() {
        val stats = TeamStatsFactory.getHomeTeamStats()
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_Standing")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("StandingPage_TabRow_Conference")
            .onNodeWithTag("StandingPage_Tab_Conference", 0)
            .performClick()
        val page = composeTestRule
            .onAllNodesWithMergedTag("StandingPage_TeamStanding_Root")[0]
        val teamNameAndIndex = page
            .onNodeWithTag("TeamStanding_LC_Standing", 0)
            .performClick()
            .onNodeWithTag("TeamStanding_Row_TeamName")
        teamNameAndIndex.apply {
            assertThat(currentState, instanceOf(NbaState.Team::class.java))
            onNodeWithTag("TeamStanding_Text_Index")
                .assertTextEquals("1")
            onNodeWithTag("TeamStanding_Text_TeamName")
                .assertTextEquals(stats.teamName)
        }
        val statsRoot = page
            .onNodeWithTag("TeamStanding_Column_StatsRoot")
            .onNodeWithTag("TeamStanding_LC_Stats")
            .onNodeWithTag("TeamStanding_Row_Stats")
        statsRoot.apply {
            viewModel.standingLabel.value.forEachIndexed { index, label ->
                onNodeWithTag("TeamStanding_Text_Stats", index)
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
                            else -> ""
                        }
                    )
            }
        }
    }

    @Test
    fun homeUser_login_checksUI() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_User")
            .performClick()
        val root = composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
        root
            .onNodeWithTag("UserPage_Btn_Login")
            .performClick()
        composeTestRule.assertDialogExist()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Login")
                .performClick()
        }
        root.assertDoesNotExist()
    }

    @Test
    fun homeUser_register_checksUI() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_User")
            .performClick()
        val root = composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
        root
            .onNodeWithTag("UserPage_Btn_Login")
            .performClick()
        composeTestRule.assertDialogExist()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Register")
                .performClick()
        }
        root.assertDoesNotExist()
    }

    @Test
    fun homeUser_logout_checksUI() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_User")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
            .onNodeWithTag("UserPage_Btn_Login")
            .performClick()
        composeTestRule.assertDialogExist()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Login")
                .performClick()
        }
        composeTestRule
            .onNodeWithMergedTag("AccountInfo_Btn_Logout")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
            .onNodeWithTag("UserPage_Btn_Login")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun homeUser_clickBetBtn_checksGoToBetListScreen() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_User")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
            .onNodeWithTag("UserPage_Btn_Login")
            .performClick()
        composeTestRule.assertDialogExist()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Login")
                .performClick()
        }
        composeTestRule
            .onNodeWithMergedTag("AccountInfo_Btn_Bet")
            .performClick()
        assertThat(currentState, instanceOf(NbaState.Bet::class.java))
    }

    @Test
    fun homeUser_checksUI() {
        composeTestRule
            .onNodeWithMergedTag("HomeBottom_Btn_User")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("UserPage_Column_NotLoginRoot")
            .onNodeWithTag("UserPage_Btn_Login")
            .performClick()
        composeTestRule.assertDialogExist()
        val loginDialog = composeTestRule
            .onDialog()
            .onNodeWithTag("LoginDialog_Dialog")
        loginDialog.apply {
            onNodeWithTag("LoginDialog_EditText_Account")
                .performTextInput(USER_ACCOUNT)
            onNodeWithTag("LoginDialog_EditText_Password")
                .performTextInput(USER_PASSWORD)
            onNodeWithTag("LoginDialog_Btn_Login")
                .performClick()
        }
        composeTestRule
            .onNodeWithMergedTag("AccountInfo_Text_AccountName")
            .assertTextEquals(USER_NAME)
        composeTestRule
            .onNodeWithMergedTag("AccountInfo_Text_Credit")
            .assertTextEquals(context.getString(R.string.user_points, USER_POINTS))
        composeTestRule
            .onNodeWithMergedTag("UserPage_LVG_Palette")
            .onNodeWithTag("UserPage_ThemeCard", 1)
            .onNodeWithTag("ThemeCard_Text_Name")
            .assertTextEquals("Blazers")
    }

    private fun getYesterdayGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GAME_CODE,
            gameId = FINAL_GAME_ID,
            gameStatus = GameStatusCode.FINAL,
            gameStatusText = GAME_STATUS_FINAL,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BASIC_TIME - 60 * 60 * 24 * 1000L),
            gameDateTime = Date(BASIC_TIME - 60 * 60 * 24 * 1000L),
            monthNum = 1,
            pointsLeaders = listOf(
                GameLeaderFactory.getHomePointsLeader(),
                GameLeaderFactory.getAwayPointsLeader()
            ),
            weekNumber = 1,
            gameLeaders = GameLeaderFactory.getGameLeaders(),
            teamLeaders = GameLeaderFactory.getGameLeaders()
        )
    }

    private fun Double.decimalFormat(radix: Int = 1): String {
        val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
        return value.toString()
    }
}