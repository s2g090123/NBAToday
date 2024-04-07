package com.jiachian.nbatoday.test.compose.screen.score

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.boxscore.ui.leader.model.BoxScoreLeaderLabel
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreScreen
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreViewModel
import com.jiachian.nbatoday.boxscore.ui.player.model.BoxScorePlayerLabel
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamLabel
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.onPopup
import com.jiachian.nbatoday.utils.tryScrollTo
import com.jiachian.nbatoday.utils.waitUntilExists
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: BoxScoreViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = BoxScoreViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.BoxScore.param to FinalGameId)),
            boxScoreUseCase = useCaseProvider.boxScore,
            dispatcherProvider = dispatcherProvider,
        )
        composeTestRule.setContent {
            BoxScoreScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun checkBoxScoreInfo() {
        val boxScore = BoxScoreGenerator.getFinal()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTopBar_Text_Date))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTopBar_Text_Date)
                .assertTextEquals(boxScore.gameDate)
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTotal_TeamInfo_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTotal_TeamInfo_Home)
                .onNodeWithTag(BoxScoreTestTag.TeamInfo_Text_TeamName)
                .assertTextEquals(boxScore.homeTeam.team.teamName)
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTotal_TeamInfo_Away)
                .onNodeWithTag(BoxScoreTestTag.TeamInfo_Text_TeamName)
                .assertTextEquals(boxScore.awayTeam.team.teamName)
            onNodeWithUnmergedTree(BoxScoreTestTag.GameScoreStatus_Text_ScoreComparison)
                .assertTextEquals(boxScore.scoreComparison)
            boxScore.homeTeam.periods.forEachIndexed { index, period ->
                onAllNodesWithUnmergedTree(BoxScoreTestTag.ScoreLabelText)[index]
                    .assertTextEquals(period.periodLabel)
            }
            onNodeWithUnmergedTree(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Home).let {
                it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_TeamName)
                    .assertTextEquals(boxScore.homeTeam.team.teamName)
                boxScore.homeTeam.periods.forEachIndexed { index, period ->
                    it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_Score, index)
                        .assertTextEquals("${period.score}")
                }
                it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_ScoreTotal)
                    .assertTextEquals("${boxScore.homeTeam.score}")
            }
            onNodeWithUnmergedTree(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Away).let {
                it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_TeamName)
                    .assertTextEquals(boxScore.awayTeam.team.teamName)
                boxScore.awayTeam.periods.forEachIndexed { index, period ->
                    it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_Score, index)
                        .assertTextEquals("${period.score}")
                }
                it.onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_ScoreTotal)
                    .assertTextEquals("${boxScore.awayTeam.score}")
            }
        }
    }

    @Test
    fun checkBoxScoreHomePlayerStats() {
        val boxScore = BoxScoreGenerator.getFinal()
        val player = boxScore.homeTeam.players.first()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home)
                .performClick()
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Home).apply {
                onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_ScorePlayerNameText)
                    .assertTextEquals(player.nameAbbr)
                onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_Text_Position)
                    .assertTextEquals(player.position)
                BoxScorePlayerLabel.values().forEachIndexed { index, label ->
                    val value = LabelHelper.getValueByLabel(label, player.statistics)
                    onNodeWithTag(BoxScoreTestTag.ScorePlayerStatsText, index)
                        .assertTextEquals(value)
                }
            }
        }
    }

    @Test
    fun checkBoxScoreAwayPlayerStats() {
        val boxScore = BoxScoreGenerator.getFinal()
        val player = boxScore.awayTeam.players.first()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Away)
                .performClick()
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Away).apply {
                onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_ScorePlayerNameText)
                    .assertTextEquals(player.nameAbbr)
                onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_Text_Position)
                    .assertTextEquals(player.position)
                BoxScorePlayerLabel.values().forEachIndexed { index, label ->
                    val value = LabelHelper.getValueByLabel(label, player.statistics)
                    onNodeWithTag(BoxScoreTestTag.ScorePlayerStatsText, index)
                        .assertTextEquals(value)
                }
            }
        }
    }

    @Test
    fun checkBoxScoreTeamStats() {
        val boxScore = BoxScoreGenerator.getFinal()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Team)
                .performClick()
            BoxScoreTeamLabel.values().forEachIndexed { index, label ->
                val homeValue = LabelHelper.getValueByLabel(label, boxScore.homeTeam.statistics)
                val awayValue = LabelHelper.getValueByLabel(label, boxScore.awayTeam.statistics)
                onAllNodesWithUnmergedTree(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Home)[index]
                    .assertTextEquals(homeValue)
                onAllNodesWithUnmergedTree(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Away)[index]
                    .assertTextEquals(awayValue)
            }
        }
    }

    @Test
    fun checkBoxScoreLeaderStats() {
        val boxScore = BoxScoreGenerator.getFinal()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Leader)
                .performClick()
            BoxScoreLeaderLabel.values().forEachIndexed { index, label ->
                val homeValue = LabelHelper.getValueByLabel(label, boxScore.homeTeam.players.first())
                val awayValue = LabelHelper.getValueByLabel(label, boxScore.awayTeam.players.first())
                onAllNodesWithUnmergedTree(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Home)[index]
                    .assertTextEquals(homeValue)
                onAllNodesWithUnmergedTree(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Away)[index]
                    .assertTextEquals(awayValue)
            }
        }
    }

    @Test
    fun clickPlayerStatsLabel_checkPopup() {
        composeTestRule.apply {
            waitUntilExists(hasTestTag(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home))
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home)
                .performClick()
            BoxScorePlayerLabel.values().forEachIndexed { index, label ->
                onAllNodesWithUnmergedTree(BoxScoreTestTag.ScorePlayerLabel)[index]
                    .tryScrollTo()
                    .performClick()
                onPopup()
                    .assertIsDisplayed()
                    .onNodeWithTag(BoxScoreTestTag.ScorePlayerLabelPopup_Text_About)
                    .assertTextEquals(getString(label.infoRes))
            }
        }
    }

    @Test
    fun clickBack_moveToBack() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTopBar_Button_Back)
                .performClick()
            navigationController.back.assertIsTrue()
        }
    }
}
