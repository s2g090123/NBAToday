package com.jiachian.nbatoday.compose.screen.bet

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import kotlin.math.abs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetScreenTest {

    private lateinit var viewModel: BetViewModel
    private lateinit var repository: TestRepository
    private var currentState: NbaState? = null
    private var isBack = false

    private val coroutineEnvironment = TestCoroutineEnvironment()

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = BetViewModel(
            account = UserAccount,
            betRepository = repository,
            openScreen = { currentState = it },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        repository.login(UserAccount, UserPassword)
        repository.refreshSchedule()
        repository.bet(FinalGameId, 0, BasicNumber.toLong())
        repository.bet(PlayingGameId, 0, BasicNumber.toLong())
        repository.bet(ComingSoonGameId, 0, BasicNumber.toLong())
        composeTestRule.setContent {
            BetScreen(
                viewModel = viewModel,
                onBackClick = { isBack = true }
            )
        }
    }

    @After
    fun teardown() {
        repository.clear()
        currentState = null
    }

    @Test
    fun betScreen_clickBackBtn_checksAction() {
        composeTestRule
            .onNodeWithTag("bet_btn_back")
            .performClick()
        assertThat(isBack, `is`(true))
    }

    @Test
    fun betScreen_emptyBetGame_checksEmptyMessageDisplayed() {
        repository.clear()
        composeTestRule
            .onNodeWithText(context.getString(R.string.bet_no_record), useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun betScreen_checksEmptyMessageNotExists() {
        composeTestRule
            .onNodeWithText(context.getString(R.string.bet_no_record), useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun betScreen_checksFinalGameDisplayed() {
        val root = composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(0)
            .onChildren()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("-0")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("+${BasicNumber * 2}")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("betCard_text_gameStatus"))
            .assertTextEquals("Final\n1:1")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertDoesNotExist()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertIsDisplayed()
    }

    @Test
    fun betScreen_checksPlayingGameDisplayed() {
        val root = composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(1)
            .onChildren()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("0")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("betCard_text_gameStatus"))
            .assertTextEquals("1:1")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertDoesNotExist()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertDoesNotExist()
    }

    @Test
    fun betScreen_checksComingSoonGameDisplayed() {
        val root = composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(2)
            .onChildren()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("0")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Points"))
            .assertTextEquals("$BasicNumber")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertDoesNotExist()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Text_Scores"))
            .assertDoesNotExist()
        root
            .filterToOne(hasTestTag("betCard_text_gameStatus"))
            .assertTextEquals("3:00\npm ET\n1:1")
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Home"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertDoesNotExist()
        root
            .filterToOne(hasTestTag("BetCard_BetCardTeamInfo_Away"))
            .onChildren()
            .filterToOne(hasTestTag("BetCardTeamInfo_Icon_Win"))
            .assertDoesNotExist()
    }

    @Test
    fun betScreen_clickFinalGame() {
        composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(0)
            .performClick()
        assertThat(viewModel.betAndGame.value.size, `is`(2))
        assertThat(repository.user.value?.points, `is`(1020))
        composeTestRule
            .onNodeWithTag("AskTurnTableDialog_Dialog", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
        val dialog = composeTestRule
            .onNodeWithTag("AskTurnTableDialog_Dialog", useUnmergedTree = true)
            .onChildren()
        dialog
            .filterToOne(hasTestTag("AskTurnTableDialog_Text_Title"))
            .assertIsDisplayed()
            .assertTextEquals(context.getString(R.string.bet_ask_turn_table_title))
        dialog
            .filterToOne(hasTestTag("AskTurnTableDialog_Text_Description"))
            .assertIsDisplayed()
            .assertTextEquals(
                context.getString(
                    R.string.bet_ask_turn_table_text,
                    BasicNumber * 2,
                    0
                )
            )
    }

    @Test
    fun betScreen_clickPlayingGame() {
        composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(1)
            .performClick()
        assertThat(currentState, instanceOf(NbaState.BoxScore::class.java))
    }

    @Test
    fun betScreen_clickComingSoonGame() {
        composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(2)
            .performClick()
        assertThat(currentState, instanceOf(NbaState.Team::class.java))
    }

    @Test
    fun betScreen_startTurnTable() = coroutineEnvironment.testScope.runTest {
        composeTestRule
            .onNodeWithTag("bet_lc_cards", useUnmergedTree = true)
            .onChildAt(0)
            .performClick()
        composeTestRule
            .onNodeWithTag("AskTurnTableDialog_Dialog", useUnmergedTree = true)
            .onChildren()
            .filterToOne(hasTestTag("AskTurnTableDialog_Btn_Continue"))
            .performClick()
        composeTestRule
            .onNodeWithTag("AskTurnTableDialog_Dialog", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("BetScreen_BetTurnTable", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
        val table = composeTestRule
            .onNodeWithTag("BetScreen_BetTurnTable", useUnmergedTree = true)
            .onChildren()
        table
            .filterToOne(hasTestTag("BetTurnTable_Text_Start"))
            .performClick()
            .assertDoesNotExist()
        val rewardPoints = getRewardPoints(
            BasicNumber * 2,
            0,
            viewModel.rewardedAngle.value
        )
        advanceUntilIdle()
        composeTestRule
            .onNodeWithTag("RewardPointDialog_dialog")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("RewardPointDialog_text_title", useUnmergedTree = true)
            .assertTextEquals(
                if (rewardPoints >= 0) context.getString(R.string.bet_reward_win_title)
                else context.getString(R.string.bet_reward_lose_title)
            )
        composeTestRule
            .onNodeWithTag("RewardPointDialog_text_body", useUnmergedTree = true)
            .assertTextEquals(
                if (rewardPoints >= 0) context.getString(
                    R.string.bet_reward_win_text,
                    abs(rewardPoints)
                )
                else context.getString(R.string.bet_reward_lose_text, abs(rewardPoints))
            )
        composeTestRule
            .onNodeWithTag("RewardPointDialog_text_ok", useUnmergedTree = true)
            .performClick()
        composeTestRule
            .onNodeWithTag("RewardPointDialog_dialog")
            .assertDoesNotExist()
    }

    private fun getRewardPoints(
        winPoints: Int,
        losePoints: Int,
        angle: Float
    ): Int {
        return when (angle) {
            in 0f..89f -> {
                -abs(winPoints) + abs(losePoints)
            }
            in 90f..179f -> {
                abs(winPoints) * 4
            }
            in 180f..269f -> {
                -abs(winPoints)
            }
            else -> {
                abs(winPoints) + abs(losePoints)
            }
        }
    }
}
