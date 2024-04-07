package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.bet.ui.main.BetScreen
import com.jiachian.nbatoday.bet.ui.main.BetViewModel
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: BetViewModel

    @Before
    fun setup() = runTest {
        useCaseProvider.user.userLogin(UserAccount, UserPassword)
        repositoryProvider.schedule.updateSchedule()
        viewModel = BetViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Bet.param to UserAccount)),
            betUseCase = useCaseProvider.bet,
            userUseCase = useCaseProvider.user,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            BetScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun betScreen_checkFinalGame() = runTest {
        repositoryProvider.bet.addBet(BetGenerator.getFinal())
        composeTestRule.let {
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Home).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("-$BetPoints")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertDoesNotExist()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertTextEquals("$BasicNumber")
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Away).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("+${2 * BetPoints}")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertIsDisplayed()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertTextEquals("$BasicNumber")
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)
                .performClick()
            viewModel.state.turnTable.status.assertIsA<TurnTableStatus.Asking>()
        }
    }

    @Test
    fun betScreen_checkPlayingGame() = runTest {
        repositoryProvider.bet.addBet(BetGenerator.getPlaying())
        composeTestRule.let {
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Home).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("$BetPoints")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertDoesNotExist()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertTextEquals("$BasicNumber")
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Away).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("$BetPoints")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertDoesNotExist()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertTextEquals("$BasicNumber")
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)
                .performClick()
            navigationController.toBoxScore.assertIs(PlayingGameId)
        }
    }

    @Test
    fun betScreen_checkComingSoonGame() = runTest {
        repositoryProvider.bet.addBet(BetGenerator.getComingSoon())
        composeTestRule.let {
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Home).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("$BetPoints")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertDoesNotExist()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertDoesNotExist()
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetCard_BetCardTeamInfo_Away).apply {
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                    .assertTextEquals("$BetPoints")
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                    .assertDoesNotExist()
                onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                    .assertDoesNotExist()
            }
            it.onNodeWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)
                .performClick()
            navigationController.toTeam.assertIs(HomeTeamId)
        }
    }

    @Test
    fun betScreen_clickBack() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BetTestTag.BetScreen_BetTop_Button_Back)
                .performClick()
            navigationController.back.assertIsTrue()
        }
    }

    @Test
    fun betScreen_noRecord() {
        composeTestRule.apply {
            onNodeWithTag(BetTestTag.BetScreen_BetEmptyText)
                .assertIsDisplayed()
        }
    }
}
