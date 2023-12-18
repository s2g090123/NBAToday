package com.jiachian.nbatoday.compose.screen.player

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting
import com.jiachian.nbatoday.compose.screen.player.models.PlayerUI
import com.jiachian.nbatoday.compose.screen.player.utils.PlayerInfoHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.player.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerId: Int,
    private val repository: PlayerRepository,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = MainRoute.Player
) {
    init {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.insertPlayer(playerId)
        }
    }

    private val player = repository.getPlayer(playerId)

    private val sortingImp = MutableStateFlow(PlayerStatsSorting.TIME_FRAME)
    val sorting = sortingImp.asStateFlow()

    val statsLabels = PlayerStatsLabel.values()

    private val statsRowData = player.map { player ->
        player?.stats?.stats?.map { stats ->
            PlayerStatsRowData(
                timeFrame = stats.timeFrame,
                teamAbbr = stats.teamNameAbbr,
                stats = stats,
                data = statsLabels.map { label ->
                    PlayerStatsRowData.Data(
                        value = LabelHelper.getValueByLabel(label, stats),
                        width = label.width,
                        align = label.align,
                        sorting = label.sorting,
                    )
                }
            )
        }
    }.flowOn(dispatcherProvider.io)

    private val sortedStatsRowData = combine(
        statsRowData,
        sorting
    ) { rowData, sorting ->
        rowData?.sortedWith(sorting)
    }.flowOn(dispatcherProvider.io)

    private val infoTableData = player.map { player ->
        player?.info?.let { info ->
            PlayerInfoHelper.getTableData(info)
        }
    }.flowOn(dispatcherProvider.io)

    private val playerUI = combine(
        player,
        infoTableData,
        sortedStatsRowData
    ) { player, info, stats ->
        if (player == null || info == null || stats == null) return@combine null
        PlayerUI(
            player = player,
            infoTableData = info,
            statsRowData = stats,
        )
    }
    val playerUIState = combine(
        repository.loading,
        playerUI
    ) { loading, playerUI ->
        if (loading) return@combine UIState.Loading()
        UIState.Loaded(playerUI)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, UIState.Loading())

    fun updateSorting(sorting: PlayerStatsSorting) {
        sortingImp.value = sorting
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
