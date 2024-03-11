package com.jiachian.nbatoday.test.compose.screen.score

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.BoxScoreScreen
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.onPopup
import com.jiachian.nbatoday.utils.tryScrollTo
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: BoxScoreViewModel

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = spyk(
            BoxScoreViewModel(
                gameId = FinalGameId,
                repository = repositoryProvider.game,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider,
            )
        )
    }

    @Composable
    override fun provideComposable(): Any {
        BoxScoreScreen(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun boxScoreScreen_checksInfoUI() = inCompose {
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTopBar_Text_Date)
            .assertTextEquals(DateUtils.formatDate(2022, 12, 30))
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTotal_TeamInfo_Home)
            .onNodeWithTag(BoxScoreTestTag.TeamInfo_Text_TeamName)
            .assertTextEquals(HomeTeamName)
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTotal_TeamInfo_Away)
            .onNodeWithTag(BoxScoreTestTag.TeamInfo_Text_TeamName)
            .assertTextEquals(AwayTeamName)
        onNodeWithUnmergedTree(BoxScoreTestTag.GameScoreStatus_Text_ScoreComparison)
            .assertTextEquals("$BasicNumber - $BasicNumber")
        onNodeWithUnmergedTree(BoxScoreTestTag.GameScoreStatus_Text_Status)
            .assertTextEquals(GameStatusFinal)
        onNodeWithUnmergedTree(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Home).apply {
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_TeamName)
                .assertTextEquals(HomeTeamName)
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_Score)
                .assertTextEquals("$BasicNumber")
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_ScoreTotal)
                .assertTextEquals("$BasicNumber")
        }
        onNodeWithUnmergedTree(BoxScoreTestTag.PeriodScoreTable_PeriodScoreRow_Away).apply {
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_TeamName)
                .assertTextEquals(AwayTeamName)
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_Score)
                .assertTextEquals("$BasicNumber")
            onNodeWithTag(BoxScoreTestTag.PeriodScoreRow_Text_ScoreTotal)
                .assertTextEquals("$BasicNumber")
        }
    }

    @Test
    fun boxScoreScreen_checksHomeStatsUI() = inCompose {
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Home)
            .performClick()
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Home).apply {
            onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_ScorePlayerNameText)
                .assertTextEquals(HomePlayerLastName)
            onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_Text_Position)
                .assertTextEquals(BasicPosition)
            BoxScorePlayerLabel.values().forEachIndexed { index, label ->
                val value = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().homeTeam.players[0].statistics)
                onNodeWithTag(BoxScoreTestTag.ScorePlayerStatsText, index)
                    .assertTextEquals(value)
            }
        }
    }

    @Test
    fun boxScoreScreen_checksAwayStatsUI() = inCompose {
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Away)
            .performClick()
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScorePlayerPage_Away).apply {
            onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_ScorePlayerNameText)
                .assertTextEquals(AwayPlayerLastName)
            onNodeWithTag(BoxScoreTestTag.ScorePlayerRow_Text_Position)
                .assertTextEquals(BasicPosition)
            BoxScorePlayerLabel.values().forEachIndexed { index, label ->
                val value = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().homeTeam.players[0].statistics)
                onNodeWithTag(BoxScoreTestTag.ScorePlayerStatsText, index)
                    .assertTextEquals(value)
            }
        }
    }

    @Test
    fun boxScoreScreen_checksTeamStatsUI() = inCompose {
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Team)
            .performClick()
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScoreTeamPage).apply {
            BoxScoreTeamLabel.values().forEachIndexed { index, label ->
                val homeValue = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().homeTeam.statistics)
                val awayValue = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().awayTeam.statistics)
                onNodeWithTag(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Home, index)
                    .assertTextEquals(homeValue)
                onNodeWithTag(BoxScoreTestTag.TeamStatsRow_ScoreTeamStatsText_Away, index)
                    .assertTextEquals(awayValue)
            }
        }
    }

    @Test
    fun boxScoreScreen_checksLeaderStatsUI() = inCompose {
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTabRow_ScoreTab_Leader)
            .performClick()
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreDetailPager_ScoreLeaderPage).apply {
            BoxScoreLeaderLabel.values().forEachIndexed { index, label ->
                val homeValue = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().homeTeam.getMostPointsPlayer())
                val awayValue = LabelHelper.getValueByLabel(label, BoxScoreGenerator.getFinal().awayTeam.getMostPointsPlayer())
                onNodeWithTag(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Home, index)
                    .assertTextEquals(homeValue)
                onNodeWithTag(BoxScoreTestTag.ScoreLeaderRow_LeaderStatsText_Away, index)
                    .assertTextEquals(awayValue)
            }
        }
    }

    @Test
    fun boxScoreScreen_checksLoading() {
        every {
            viewModel.boxScoreUIState
        } returns MutableStateFlow(UIState.Loading())
        inCompose {
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreScreen_Loading)
                .assertIsDisplayed()
        }
    }

    @Test
    fun boxScoreScreen_checksNotFound() {
        every {
            viewModel.boxScoreUIState
        } returns MutableStateFlow(UIState.Loaded(null))
        inCompose {
            onNodeWithUnmergedTree(BoxScoreTestTag.ScoreScreen_NotFoundScreen)
                .assertIsDisplayed()
        }
    }

    @Test
    fun boxScoreScreen_clicksLabel_checksPopup() = inCompose {
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

    @Test
    fun boxScoreScreen_clicksBack() = inCompose {
        val event = navigationController.eventFlow.defer(it)
        onNodeWithUnmergedTree(BoxScoreTestTag.ScoreTopBar_Button_Back)
            .performClick()
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            .departure
            .assertIs(MainRoute.BoxScore)
    }
}
