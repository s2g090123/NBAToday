package com.jiachian.nbatoday.compose.screen.player

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting
import com.jiachian.nbatoday.compose.screen.player.utils.PlayerInfoHelper
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.player.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    route = Route.PLAYER
) {
    val isLoading = repository.isLoading

    init {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.updatePlayer(playerId)
        }
    }

    val player = repository.getPlayer(playerId)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    val notFound = player.map {
        it == null
    }.stateIn(coroutineScope, SharingStarted.Lazily, true)

    private val statsSortingImp = MutableStateFlow(PlayerStatsSorting.TIME_FRAME)
    val statsSorting = statsSortingImp.asStateFlow()

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
        } ?: emptyList()
    }

    val sortedStatsRowData = combine(
        statsRowData,
        statsSorting
    ) { rowData, sorting ->
        rowData.sortedWith(sorting)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val playerInfoTableData = player.map {
        val info = it?.info ?: return@map null
        PlayerInfoHelper.createPlayerInfoTableData(info)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    fun updateStatsSorting(sorting: PlayerStatsSorting) {
        statsSortingImp.value = sorting
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
