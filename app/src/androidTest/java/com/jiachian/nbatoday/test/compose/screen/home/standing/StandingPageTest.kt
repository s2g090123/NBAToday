package com.jiachian.nbatoday.test.compose.screen.home.standing

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.home.standing.ui.StandingPage
import com.jiachian.nbatoday.home.standing.ui.StandingPageViewModel
import com.jiachian.nbatoday.home.standing.ui.model.StandingLabel
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.testing.testtag.StandingTestTag
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StandingPageTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: StandingPageViewModel

    @Before
    fun setup() {
        viewModel = StandingPageViewModel(
            teamUseCase = useCaseProvider.team,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            StandingPage(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun eastStanding_checkRowData() = runTest {
        repositoryProvider.team.addTeams()
        val team = TeamGenerator.getHome()
        composeTestRule.let {
            it.onNodeWithText(context.getString(NBATeam.Conference.EAST.textRes)).performClick()
            it.onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0].apply {
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(team.team.teamName)
                    .performClick()
                navigationController.toTeam.assertIs(team.teamId)
                StandingLabel.values().forEachIndexed { index, label ->
                    val value = LabelHelper.getValueByLabel(label, team)
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(value)
                }
            }
        }
    }

    @Test
    fun westStanding_checkRowData() = runTest {
        repositoryProvider.team.addTeams()
        val team = TeamGenerator.getAway()
        composeTestRule.let {
            it.onNodeWithText(context.getString(NBATeam.Conference.WEST.textRes)).performClick()
            it.onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0].apply {
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(team.team.teamName)
                    .performClick()
                navigationController.toTeam.assertIs(team.teamId)
                StandingLabel.values().forEachIndexed { index, label ->
                    val value = LabelHelper.getValueByLabel(label, team)
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(value)
                }
            }
        }
    }

    @Test
    fun swipeRefresh_checkRowDataUpdated() {
        val team = TeamGenerator.getHome()
        composeTestRule.let {
            it.onNodeWithText(context.getString(NBATeam.Conference.EAST.textRes)).performClick()
            it.onAllNodesWithUnmergedTree(StandingTestTag.StandingPage_Content)[0].apply {
                performTouchInput { swipeDown() }
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_Standing)
                    .assertTextEquals("1.")
                onNodeWithTag(StandingTestTag.StandingTeamRow_Text_TeamName)
                    .assertTextEquals(team.team.teamName)
                    .performClick()
                navigationController.toTeam.assertIs(team.teamId)
                StandingLabel.values().forEachIndexed { index, label ->
                    val value = LabelHelper.getValueByLabel(label, team)
                    onNodeWithTag(StandingTestTag.StandingStatsText, index)
                        .assertTextEquals(value)
                }
            }
        }
    }
}
