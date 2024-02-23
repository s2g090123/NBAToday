package com.jiachian.nbatoday.test.compose.screen.bet

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.compose.screen.bet.BetScreen
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetScreenTest : BaseAndroidTest() {
    @Composable
    override fun provideComposable(): Any {
        BetScreen(
            viewModel = BetViewModel(
                account = UserAccount,
                repository = repositoryProvider.bet,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider
            )
        )
        return super.provideComposable()
    }

    @Before
    fun setup() = runTest {
        repositoryProvider.user.login(UserAccount, UserPassword)
        repositoryProvider.schedule.updateSchedule()
    }

    @Test
    fun betScreen_checkLoadingUI() {
        val viewModel = spyk(
            BetViewModel(
                account = UserAccount,
                repository = repositoryProvider.bet,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider
            )
        )
        every {
            viewModel.betsAndGamesState
        } returns MutableStateFlow(UIState.Loading())
        composeTestRule.setContent {
            BetScreen(viewModel = viewModel)
        }
        composeTestRule
            .onNodeWithUnmergedTree(BetTestTag.BetScreen_BetBody_Loading)
            .assertIsDisplayed()
        unmockkObject(viewModel)
    }

    @Test
    fun betScreen_checksEmptyUI() = inCompose {
        onNodeWithUnmergedTree(BetTestTag.BetScreen_BetEmptyScreen)
            .assertIsDisplayed()
    }

    @Test
    fun betScreen_checksFinalGameUI() = inCompose {
        insertBets()
        onNodeWithUnmergedTree(BetTestTag.BetScreen_BetEmptyScreen)
            .assertDoesNotExist()
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[0]
            .apply {
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Home)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("-$BetPoints")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertTextEquals(BasicNumber.toString())
                    }
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Away)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("+${2 * BetPoints}")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertTextEquals(BasicNumber.toString())
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                            .assertIsDisplayed()
                    }
                onNodeWithTag(BetTestTag.BetCard_Text_GameStatus)
                    .assertTextEquals("$GameStatusFinal\n1:1")
            }
    }

    @Test
    fun betScreen_clicksFinalGame_displaysAskTurnTable() = inCompose {
        insertBets()
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[0]
            .performClick()
        onNodeWithUnmergedTree(BetTestTag.AskTurnTableDialog)
            .assertIsDisplayed()
    }

    @Test
    fun betScreen_checksPlayingGameUI() = inCompose {
        insertBets()
        onNodeWithUnmergedTree(BetTestTag.BetScreen_BetEmptyScreen)
            .assertDoesNotExist()
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[1]
            .apply {
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Home)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("$BetPoints")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertTextEquals(BasicNumber.toString())
                    }
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Away)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("$BetPoints")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertTextEquals(BasicNumber.toString())
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                            .assertDoesNotExist()
                    }
                onNodeWithTag(BetTestTag.BetCard_Text_GameStatus)
                    .assertTextEquals("1:1")
            }
    }

    @Test
    fun betScreen_clicksPlayingGame_navigateToBoxScore() = inCompose {
        insertBets()
        val event = navigationController.eventFlow.defer(it)
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[1]
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            .gameId
            .assertIs(PlayingGameId)
    }

    @Test
    fun betScreen_checksComingSoonGameUI() = inCompose {
        insertBets()
        onNodeWithUnmergedTree(BetTestTag.BetScreen_BetEmptyScreen)
            .assertDoesNotExist()
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[2]
            .apply {
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Home)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("$BetPoints")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertDoesNotExist()
                    }
                onNodeWithTag(BetTestTag.BetCard_BetCardTeamInfo_Away)
                    .apply {
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Points)
                            .assertTextEquals("$BetPoints")
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Text_Scores)
                            .assertDoesNotExist()
                        onNodeWithTag(BetTestTag.BetCardTeamInfo_Icon_Crown)
                            .assertDoesNotExist()
                    }
                onNodeWithTag(BetTestTag.BetCard_Text_GameStatus)
                    .assertTextEquals("${GameStatusPrepare.replaceFirst(" ", "\n")}\n1:1")
            }
    }

    @Test
    fun betScreen_clicksComingSoonGame_navigateToTeam() = inCompose {
        insertBets()
        val event = navigationController.eventFlow.defer(it)
        onAllNodesWithUnmergedTree(BetTestTag.BetScreen_BetBody_BetCard)[2]
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
            .teamId
            .assertIs(HomeTeamId)
    }

    @Test
    fun betScreen_clicksBack_backScreenIsTriggered() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithUnmergedTree(BetTestTag.BetScreen_BetTop_Button_Back)
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            .departure
            .assertIs(MainRoute.Bet)
    }

    private suspend fun insertBets() {
        repositoryProvider.bet.insertBet(FinalGameId, BetPoints, BetPoints)
        repositoryProvider.bet.insertBet(PlayingGameId, BetPoints, BetPoints)
        repositoryProvider.bet.insertBet(ComingSoonGameId, BetPoints, BetPoints)
    }
}
