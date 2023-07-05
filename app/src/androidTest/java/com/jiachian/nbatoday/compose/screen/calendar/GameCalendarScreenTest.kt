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
import com.jiachian.nbatoday.BASIC_TIME
import com.jiachian.nbatoday.COMING_SOON_GAME_ID
import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.GAME_CODE
import com.jiachian.nbatoday.GAME_STATUS_FINAL
import com.jiachian.nbatoday.GAME_STATUS_PREPARE
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.GameTeamFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.getOrAssert
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GameCalendarScreenTest {
    private lateinit var viewModel: GameCalendarViewModel
    private lateinit var repository: TestRepository
    private var currentState: NbaState? = null
    private var isBack = false

    private val coroutineEnvironment = TestCoroutineEnvironment()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = GameCalendarViewModel(
            date = Date(BASIC_TIME),
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
            GameCalendarScreen(
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
            .onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
            .assertTextEquals(firstGame.game.homeTeam.teamTricode)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
            .assertTextEquals(firstGame.game.awayTeam.teamTricode)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeScore")
            .assertTextEquals(firstGame.game.homeTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayScore")
            .assertTextEquals(firstGame.game.awayTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(firstGame.game.gameStatusText)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_Expand")
            .assertDoesNotExist()

        val secondCardRoot = gamesRoot
            .onChildren()
            .assertCountEquals(games.size)[1]
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
            .assertTextEquals(secondGame.game.homeTeam.teamTricode)
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
            .assertTextEquals(secondGame.game.awayTeam.teamTricode)
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeScore")
            .assertTextEquals(secondGame.game.homeTeam.score.toString())
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayScore")
            .assertTextEquals(secondGame.game.awayTeam.score.toString())
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(secondGame.game.gameStatusText)
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        secondCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_Expand")
            .assertDoesNotExist()
    }

    @Test
    fun calendar_checksTomorrowGames() {
        val gameDateRoot = composeTestRule
            .onNodeWithMergedTag("CalendarContent_LVG_Games")
            .onChildAt(1)
            .performClick()
        val games = viewModel.selectGames.value.getOrAssert()
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
            .onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
            .assertTextEquals(game.game.homeTeam.teamTricode)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
            .assertTextEquals(game.game.awayTeam.teamTricode)
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeScore")
            .assertTextEquals(game.game.homeTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayScore")
            .assertTextEquals(game.game.awayTeam.score.toString())
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(game.game.gameStatusText.replaceFirst(" ", "\n"))
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        firstCardRoot
            .onNodeWithTag("GameStatusCard2_Btn_Expand")
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
            .onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
            .assertTextEquals(lastGame.homeTeam.teamTricode)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
            .assertTextEquals(lastGame.awayTeam.teamTricode)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeScore")
            .assertTextEquals(lastGame.homeTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayScore")
            .assertTextEquals(lastGame.awayTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(lastGame.gameStatusText)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_Expand")
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
            .onNodeWithTag("GameStatusCard2_Text_HomeTriCode")
            .assertTextEquals(nextGame.homeTeam.teamTricode)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayTriCode")
            .assertTextEquals(nextGame.awayTeam.teamTricode)
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_HomeScore")
            .assertTextEquals(nextGame.homeTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_AwayScore")
            .assertTextEquals(nextGame.awayTeam.score.toString())
        cardRoot
            .onNodeWithTag("GameStatusCard2_Text_GameStatus")
            .assertTextEquals(nextGame.gameStatusText.replaceFirst(" ", "\n"))
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_bet")
            .assertDoesNotExist()
        cardRoot
            .onNodeWithTag("GameStatusCard2_Btn_Expand")
            .assertDoesNotExist()
    }

    private fun getLastMonthGame(): NbaGame {
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
            gameDate = Date(BASIC_TIME - 60 * 60 * 24 * 30 * 1000L),
            gameDateTime = Date(BASIC_TIME - 60 * 60 * 24 * 30 * 1000L),
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

    private fun getNextMonthGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GAME_CODE,
            gameId = COMING_SOON_GAME_ID,
            gameStatus = GameStatusCode.COMING_SOON,
            gameStatusText = GAME_STATUS_PREPARE,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BASIC_TIME + 60 * 60 * 24 * 40 * 1000L),
            gameDateTime = Date(BASIC_TIME + 60 * 60 * 24 * 40 * 1000L),
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