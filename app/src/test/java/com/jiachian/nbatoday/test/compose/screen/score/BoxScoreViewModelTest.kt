package com.jiachian.nbatoday.test.compose.screen.score

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: BoxScoreViewModel

    private val boxScoreAndGame: BoxScoreAndGame
        get() {
            return BoxScoreAndGame(
                boxScore = BoxScoreGenerator.getFinal(),
                game = GameGenerator.getFinal(),
            )
        }

    private val boxScore: BoxScore
        get() = boxScoreAndGame.boxScore

    private val periods: List<String>
        get() {
            return boxScore.homeTeam.periods.map { it.periodLabel }
        }

    private val playersUI: BoxScoreUI.BoxScorePlayersUI
        get() {
            val homeRowData = boxScore.homeTeam.players.map { player ->
                BoxScorePlayerRowData(
                    player = player,
                    data = BoxScorePlayerLabel.values().map { label ->
                        BoxScorePlayerRowData.Data(
                            value = LabelHelper.getValueByLabel(label, player.statistics),
                            width = label.width,
                            align = label.align,
                        )
                    }
                )
            }
            val awayRowData = boxScore.awayTeam.players.map { player ->
                BoxScorePlayerRowData(
                    player = player,
                    data = BoxScorePlayerLabel.values().map { label ->
                        BoxScorePlayerRowData.Data(
                            value = LabelHelper.getValueByLabel(label, player.statistics),
                            width = label.width,
                            align = label.align,
                        )
                    }
                )
            }
            return BoxScoreUI.BoxScorePlayersUI(homeRowData, awayRowData)
        }

    private val teamsUI: BoxScoreUI.BoxScoreTeamsUI
        get() {
            val rowData = BoxScoreTeamLabel.values().map { label ->
                BoxScoreTeamRowData(
                    label = label,
                    home = LabelHelper.getValueByLabel(label, boxScore.homeTeam.statistics),
                    away = LabelHelper.getValueByLabel(label, boxScore.awayTeam.statistics),
                )
            }
            return BoxScoreUI.BoxScoreTeamsUI(
                home = boxScore.homeTeam.team,
                away = boxScore.awayTeam.team,
                rowData = rowData
            )
        }

    private val leadersUI: BoxScoreUI.BoxScoreLeadersUI
        get() {
            val home = boxScore.homeTeam.players.first()
            val away = boxScore.awayTeam.players.first()
            val rowData = BoxScoreLeaderLabel.values().map { label ->
                BoxScoreLeaderRowData(
                    label = label,
                    home = LabelHelper.getValueByLabel(label, home),
                    away = LabelHelper.getValueByLabel(label, away),
                )
            }
            return BoxScoreUI.BoxScoreLeadersUI(
                home = home,
                away = away,
                rowData = rowData,
            )
        }

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        viewModel = BoxScoreViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.BoxScore.param to boxScoreAndGame.game.gameId)),
            repository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `playerLabels expects correct`() {
        assertIs(viewModel.playerLabels, BoxScorePlayerLabel.values())
    }

    @Test
    fun `date expects correct`() {
        assertIs(viewModel.date.value, "")
        viewModel.date.launchAndCollect()
        assertIs(viewModel.date.value, boxScoreAndGame.boxScore.gameDate)
    }

    @Test
    fun `boxScoreUIState expects correct`() {
        assertIsA(
            viewModel.boxScoreUIState.value,
            UIState.Loading::class.java
        )
        viewModel.boxScoreUIState.launchAndCollect()
        val actual = viewModel.boxScoreUIState.value.getDataOrNull()
        val expected = BoxScoreUI(
            boxScore = boxScore,
            periods = periods,
            players = playersUI,
            teams = teamsUI,
            leaders = leadersUI,
        )
        assertIs(actual, expected)
    }

    @Test
    fun `selectPlayerLabel() expects selectedPlayerLabel is updated`() {
        BoxScorePlayerLabel.values().forEach { label ->
            viewModel.selectPlayerLabel(label)
            assertIs(viewModel.selectedPlayerLabel.value, label)
        }
    }
}
