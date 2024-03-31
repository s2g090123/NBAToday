package com.jiachian.nbatoday.test.compose.screen.player

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.player.ui.model.PlayerInfoTableData
import com.jiachian.nbatoday.player.ui.model.PlayerStatsLabel
import com.jiachian.nbatoday.player.ui.model.PlayerStatsRowData
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting
import com.jiachian.nbatoday.player.ui.model.PlayerTableLabel
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.get

class PlayerViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: PlayerViewModel

    private val player: Player
        get() = PlayerGenerator.getHome()

    private val rowData: List<PlayerStatsRowData>
        get() {
            return player.stats.stats.map { stats ->
                PlayerStatsRowData(
                    timeFrame = stats.timeFrame,
                    teamAbbr = stats.teamNameAbbr,
                    stats = stats,
                    data = PlayerStatsLabel.values().map { label ->
                        PlayerStatsRowData.Data(
                            value = LabelHelper.getValueByLabel(label, stats),
                            width = label.width,
                            align = label.align,
                            sorting = label.sorting,
                        )
                    }
                )
            }.sortedWith(viewModel.sorting.value)
        }

    private val infoTableData: PlayerInfoTableData
        get() = PlayerInfoTableData(
            rowData = PlayerTableLabel.values().map { label ->
                PlayerInfoTableData.RowData.Data(
                    titleRes = label.titleRes,
                    value = LabelHelper.getValueByLabel(label, player.info)
                )
            }
                .chunked(3)
                .map {
                    PlayerInfoTableData.RowData(it)
                }
        )

    @Before
    fun setup() {
        viewModel = PlayerViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Player.param to "${player.playerId}")),
            repository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `statsLabels expects correct`() {
        assertIs(viewModel.statsLabels, PlayerStatsLabel.values())
    }

    @Test
    fun `playerUIState expects correct`() {
        assertIsA(
            viewModel.playerUIState.value,
            UIState.Loading::class.java
        )
        viewModel.playerUIState.launchAndCollect()
        assertIsA(
            viewModel.playerUIState.value,
            UIState.Loaded::class.java
        )
        val actual = viewModel.playerUIState.value.getDataOrNull()
        val expected = PlayerUI(
            player = player,
            infoTableData = infoTableData,
            statsRowData = rowData,
        )
        assertIs(actual, expected)
    }

    @Test
    fun `updateSorting() expects sorting and uiState are updated`() {
        viewModel.playerUIState.launchAndCollect()
        PlayerStatsSorting.values().forEach { sorting ->
            viewModel.updateSorting(sorting)
            assertIs(viewModel.sorting.value, sorting)
            val actual = viewModel.playerUIState.value.getDataOrNull()
            val expected = PlayerUI(
                player = player,
                infoTableData = infoTableData,
                statsRowData = rowData,
            )
            assertIs(actual, expected)
        }
    }

    private fun List<PlayerStatsRowData>.sortedWith(sorting: PlayerStatsSorting): List<PlayerStatsRowData> {
        val comparator = when (sorting) {
            PlayerStatsSorting.TIME_FRAME -> compareByDescending { it.timeFrame }
            PlayerStatsSorting.GP -> compareByDescending { it.stats.gamePlayed }
            PlayerStatsSorting.W -> compareByDescending { it.stats.win }
            PlayerStatsSorting.L -> compareBy { it.stats.lose }
            PlayerStatsSorting.WINP -> compareByDescending { it.stats.winPercentage }
            PlayerStatsSorting.PTS -> compareByDescending { it.stats.pointsAverage }
            PlayerStatsSorting.FGM -> compareByDescending { it.stats.fieldGoalsMadeAverage }
            PlayerStatsSorting.FGA -> compareByDescending { it.stats.fieldGoalsAttemptedAverage }
            PlayerStatsSorting.FGP -> compareByDescending { it.stats.fieldGoalsPercentage }
            PlayerStatsSorting.PM3 -> compareByDescending { it.stats.threePointersMadeAverage }
            PlayerStatsSorting.PA3 -> compareByDescending { it.stats.threePointersAttemptedAverage }
            PlayerStatsSorting.PP3 -> compareByDescending { it.stats.threePointersPercentage }
            PlayerStatsSorting.FTM -> compareByDescending { it.stats.freeThrowsMadeAverage }
            PlayerStatsSorting.FTA -> compareByDescending { it.stats.freeThrowsAttemptedAverage }
            PlayerStatsSorting.FTP -> compareByDescending { it.stats.freeThrowsPercentage }
            PlayerStatsSorting.OREB -> compareByDescending { it.stats.reboundsOffensiveAverage }
            PlayerStatsSorting.DREB -> compareByDescending { it.stats.reboundsDefensiveAverage }
            PlayerStatsSorting.REB -> compareByDescending { it.stats.reboundsTotalAverage }
            PlayerStatsSorting.AST -> compareByDescending { it.stats.assistsAverage }
            PlayerStatsSorting.TOV -> compareBy { it.stats.turnoversAverage }
            PlayerStatsSorting.STL -> compareByDescending { it.stats.stealsAverage }
            PlayerStatsSorting.BLK -> compareByDescending { it.stats.blocksAverage }
            PlayerStatsSorting.PF -> compareBy { it.stats.foulsPersonalAverage }
            PlayerStatsSorting.PLUSMINUS -> compareByDescending<PlayerStatsRowData> { it.stats.plusMinus }
        }.thenByDescending { it.stats.winPercentage }
        return sortedWith(comparator)
    }
}
