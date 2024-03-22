package com.jiachian.nbatoday.test.compose.screen.home.standing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.GamePlayed
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPage
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.testing.testtag.StandingTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StandingPageTest : BaseAndroidTest() {
    private lateinit var viewModel: StandingPageViewModel

    @Composable
    override fun ProvideComposable() {
        StandingPage(
            viewModel = viewModel,
            navigateToTeam = {
                // do nothing
            }
        )
    }

    @Before
    fun setup() = runTest {
        repositoryProvider.team.addTeams()
        viewModel = StandingPageViewModel(
            repository = repositoryProvider.team,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun standingPage_switchesEast_checksUI() = inCompose {
        onAllNodesWithUnmergedTree(StandingTestTag.StandingTabRow_Tab)[0]
            .performClick()
        onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0]
            .onNodeWithTag(StandingTestTag.StandingStatsRow, 0).apply {
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(HomeTeamName)
                StandingLabel.values().forEachIndexed { index, label ->
                    val text = when (label) {
                        StandingLabel.GP -> GamePlayed
                        StandingLabel.W -> BasicNumber
                        StandingLabel.L -> BasicNumber
                        StandingLabel.WINP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PTS -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.FGP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PP3 -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.FTP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.OREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.DREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.AST -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.TOV -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.STL -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.BLK -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                    }.toString()
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(text)
                }
            }
    }

    @Test
    fun standingPage_switchesWest_checksUI() = inCompose {
        onAllNodesWithUnmergedTree(StandingTestTag.StandingTabRow_Tab)[1]
            .performClick()
        onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0]
            .onNodeWithTag(StandingTestTag.StandingStatsRow, 0).apply {
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(AwayTeamName)
                StandingLabel.values().forEachIndexed { index, label ->
                    val text = when (label) {
                        StandingLabel.GP -> GamePlayed
                        StandingLabel.W -> BasicNumber
                        StandingLabel.L -> BasicNumber
                        StandingLabel.WINP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PTS -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.FGP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PP3 -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.FTP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.OREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.DREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.AST -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.TOV -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.STL -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.BLK -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                    }.toString()
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(text)
                }
            }
    }

    @Test
    fun standingPage_updateSorting() = inCompose {
        onAllNodesWithUnmergedTree(StandingTestTag.StandingTabRow_Tab)[0]
            .performClick()
        onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0]
            .onNodeWithTag(StandingTestTag.StandingLabel)
            .performClick()
        onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0]
            .onNodeWithTag(StandingTestTag.StandingStatsRow, 0).apply {
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(HomeTeamName)
                StandingLabel.values().forEachIndexed { index, label ->
                    val text = when (label) {
                        StandingLabel.GP -> GamePlayed
                        StandingLabel.W -> BasicNumber
                        StandingLabel.L -> BasicNumber
                        StandingLabel.WINP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PTS -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.FGP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.PP3 -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.FTP -> (BasicNumber / GamePlayed.toDouble() * 100).decimalFormat()
                        StandingLabel.OREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.DREB -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.AST -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.TOV -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.STL -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                        StandingLabel.BLK -> (BasicNumber / GamePlayed.toDouble()).decimalFormat()
                    }.toString()
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(text)
                }
            }
    }

    @Test
    fun standingPage_checksRefreshingUI() = inCompose {
        dataHolder.teams.value = emptyList()
        onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)
            .onFirst()
            .performTouchInput { swipeDown() }
        dataHolder
            .teams
            .value
            .assertIs(
                listOf(
                    TeamGenerator.getHome(),
                    TeamGenerator.getAway()
                )
            )
    }
}
