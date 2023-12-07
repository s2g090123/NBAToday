package com.jiachian.nbatoday.compose.screen.calendar

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.models.GameLeaderFactory
import com.jiachian.nbatoday.models.GameTeamFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import java.util.Date
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameCalendarScreenTest {
    private lateinit var viewModel: CalendarViewModel
    private lateinit var repository: TestRepository
    private var currentState: NbaState? = null
    private var isBack = false

    private val coroutineEnvironment = TestCoroutineEnvironment()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = CalendarViewModel(
            date = Date(BasicTime),
            repository = repository,
            openScreen = {
                currentState = it
            },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        repository.refreshSchedule()
        repository.insertGame(getLastMonthGame())
        repository.insertGame(getNextMonthGame())
        composeTestRule.setContent {
            CalendarScreen(
                viewModel = viewModel,
                onClose = { isBack = true }
            )
        }
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun calendar_clickCloseBtn_checksAction() {
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Close")
            .performClick()
        assertThat(isBack, `is`(true))
    }

    @Test
    fun calendar_checksTodayGames() {
        assertTrue(viewModel.gamesData.value.isNotEmpty())
        val games = viewModel.gamesData.value.first()
        assertTrue(games.size > 1)
        val firstGame = games[0]
        val secondGame = games[1]
        composeTestRule
            .onNodeWithMergedTag("CalendarTopBar_Text_Date")
            .assertTextEquals("Jan  2023")
        val gameDateRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_LVG_Games")
            .onChildAt(0)
            .performClick()
        gameDateRoot
            .onNodeWithTag("CalendarContent_Text_Date")
            .assertTextContains("1")
        val gamesIconRoot = gameDateRoot
            .onNodeWithTag("CalendarContent_FlowRow_Games")
            .assertExists()
        gamesIconRoot
            .onChildren()
            .assertCountEquals(games.size)
        gamesIconRoot
            .onNodeWithTag("CalendarContent_Image_Team")
            .assertExists()
            .assertIsDisplayed()
        val gamesRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_CalendarGames")
            .assertExists()
            .assertIsDisplayed()
        val firstCardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(games.size)
            .onFirst()
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(firstGame.game.homeTeam.team.abbreviation)
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(firstGame.game.awayTeam.team.abbreviation)
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(firstGame.game.homeTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(firstGame.game.awayTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(firstGame.game.gameStatusText)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        firstCardRoot
            .onNodeWithTag("ExpandContent_Btn_Expand")
            .assertDoesNotExist()

        val secondCardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(games.size)[1]
        secondCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(secondGame.game.homeTeam.team.abbreviation)
        secondCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(secondGame.game.awayTeam.team.abbreviation)
        secondCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(secondGame.game.homeTeam.score.toString())
        secondCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(secondGame.game.awayTeam.score.toString())
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(secondGame.game.gameStatusFormatText)
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        secondCardRoot
            .onNodeWithTag("ExpandContent_Btn_Expand")
            .assertDoesNotExist()
    }

    @Test
    fun calendar_checksTomorrowGames() {
        val gameDateRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_LVG_Games")
            .onChildAt(1)
            .performClick()
        val games = viewModel.selectGames.value.getOrError()
        assertTrue(games.isNotEmpty())
        val game = games.first()
        gameDateRoot
            .onNodeWithTag("CalendarContent_Text_Date")
            .assertTextContains("2")
        val gamesIconRoot = gameDateRoot
            .onNodeWithTag("CalendarContent_FlowRow_Games")
            .assertExists()
        gamesIconRoot
            .onChildren()
            .assertCountEquals(games.size)
        gamesIconRoot
            .onNodeWithTag("CalendarContent_Image_Team")
            .assertExists()
            .assertIsDisplayed()
        val gamesRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_CalendarGames")
            .assertExists()
            .assertIsDisplayed()
        val firstCardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(games.size)
            .onFirst()
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(game.game.homeTeam.team.abbreviation)
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(game.game.awayTeam.team.abbreviation)
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(game.game.homeTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(game.game.awayTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(game.game.gameStatusText.replaceFirst(" ", "\n"))
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        firstCardRoot
            .onNodeWithTag("ExpandContent_Btn_Expand")
            .assertDoesNotExist()
    }

    @Test
    fun calendar_clickPrevBtn_checksAction() {
        val lastGame = getLastMonthGame()
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Prev")
            .assertIsEnabled()
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("CalendarTopBar_Text_Date")
            .assertTextEquals("Dec  2022")
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Prev")
            .assertIsNotEnabled()
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Next")
            .assertIsEnabled()
        assertTrue(viewModel.gamesData.value.size > 5)
        assertTrue(viewModel.gamesData.value[5].isNotEmpty())
        val gameDateRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_LVG_Games")
            .onChildAt(5)
            .performClick()
        gameDateRoot
            .onNodeWithTag("CalendarContent_Text_Date")
            .assertTextContains("2")
        val gamesIconRoot = gameDateRoot
            .onNodeWithTag("CalendarContent_FlowRow_Games")
            .assertExists()
        gamesIconRoot
            .onNodeWithTag("CalendarContent_Image_Team")
            .assertExists()
            .assertIsDisplayed()
        val gamesRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_CalendarGames")
            .assertExists()
            .assertIsDisplayed()
        val cardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(1)
            .onFirst()
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(lastGame.homeTeam.team.abbreviation)
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(lastGame.awayTeam.team.abbreviation)
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(lastGame.homeTeam.score.toString())
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(lastGame.awayTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(lastGame.gameStatusText)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        cardRoot
            .onNodeWithTag("ExpandContent_Btn_Expand")
            .assertDoesNotExist()
    }

    @Test
    fun calendar_clickNextBtn_checksAction() {
        val nextGame = getNextMonthGame()
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Next")
            .assertIsEnabled()
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("CalendarTopBar_Text_Date")
            .assertTextEquals("Feb  2023")
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Prev")
            .assertIsEnabled()
        composeTestRule
            .onNodeWithTag("CalendarTopBar_Btn_Next")
            .assertIsNotEnabled()
        assertTrue(viewModel.gamesData.value.size > 12)
        assertTrue(viewModel.gamesData.value[12].isNotEmpty())
        val gameDateRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_LVG_Games")
            .onChildAt(12)
            .performClick()
        gameDateRoot
            .onNodeWithTag("CalendarContent_Text_Date")
            .assertTextContains("10")
        val gamesIconRoot = gameDateRoot
            .onNodeWithTag("CalendarContent_FlowRow_Games")
            .assertExists()
        gamesIconRoot
            .onNodeWithTag("CalendarContent_Image_Team")
            .assertExists()
            .assertIsDisplayed()
        val gamesRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_CalendarGames")
            .assertExists()
            .assertIsDisplayed()
        val cardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(1)
            .onFirst()
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(nextGame.homeTeam.team.abbreviation)
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_TriCode")
            .assertTextEquals(nextGame.awayTeam.team.abbreviation)
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(nextGame.homeTeam.score.toString())
        cardRoot
            .onNodeWithTag("ScoreBoard_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Score")
            .assertTextEquals(nextGame.awayTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(nextGame.gameStatusText.replaceFirst(" ", "\n"))
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        cardRoot
            .onNodeWithTag("ExpandContent_Btn_Expand")
            .assertDoesNotExist()
    }

    private fun getLastMonthGame(): Game {
        return Game(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BasicTime - 60 * 60 * 24 * 30 * 1000L),
            gameDateTime = Date(BasicTime - 60 * 60 * 24 * 30 * 1000L),
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

    private fun getNextMonthGame(): Game {
        return Game(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON,
            gameStatusText = GameStatusPrepare,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BasicTime + 60 * 60 * 24 * 40 * 1000L),
            gameDateTime = Date(BasicTime + 60 * 60 * 24 * 40 * 1000L),
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
}
