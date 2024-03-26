package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.player.event.PlayerDataEvent
import com.jiachian.nbatoday.compose.screen.player.event.PlayerUIEvent
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerTableLabel
import com.jiachian.nbatoday.compose.screen.player.state.MutablePlayerState
import com.jiachian.nbatoday.compose.screen.player.state.PlayerState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.player.PlayerUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val playerUseCase: PlayerUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val playerId: Int = savedStateHandle.get<String>(MainRoute.Player.param)?.toIntOrNull() ?: throw Exception("playerId is null.")

    private val stateImp = MutablePlayerState(playerId)
    val state: PlayerState = stateImp

    init {
        collectPlayer()
        updatePlayer()
    }

    private fun collectPlayer() {
        viewModelScope.launch {
            snapshotFlow {
                state.stats.sorting
            }.flatMapLatest { sorting ->
                playerUseCase
                    .getPlayer(playerId, sorting)
                    .flowOn(dispatcherProvider.default)
            }.collectLatest { player ->
                if (player == null) {
                    stateImp.notFound = true
                } else {
                    val (tableData, rowData) = withContext(dispatcherProvider.default) {
                        player.info.toTableData() to player.stats.stats.toRowData()
                    }
                    Snapshot.withMutableSnapshot {
                        stateImp.apply {
                            info.let { state ->
                                state.name = player.info.playerName
                                state.team = player.info.team
                                state.detail = player.info.detail
                                state.is75 = player.info.isGreatest75
                                state.data = tableData
                            }
                            stats.data = rowData
                            notFound = false
                        }
                    }
                }
            }
        }
    }

    private fun updatePlayer() {
        viewModelScope.launch {
            playerUseCase
                .addPlayer(playerId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            Snapshot.withMutableSnapshot {
                                stateImp.apply {
                                    event = PlayerDataEvent.Error(resource.error.asPlayerError())
                                    loading = false
                                }
                            }
                        }
                        is Resource.Loading -> stateImp.loading = true
                        is Resource.Success -> {
                            delay(300) // delay for fetching new data
                            stateImp.loading = false
                        }
                    }
                }
        }
    }

    fun onEvent(event: PlayerUIEvent) {
        when (event) {
            PlayerUIEvent.EventReceived -> stateImp.event = null
            is PlayerUIEvent.Sort -> stateImp.stats.sorting = event.sorting
        }
    }

    private fun List<Player.PlayerStats.Stats>.toRowData(): List<PlayerStatsRowData> {
        return map { stats ->
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
        }
    }

    private fun Player.PlayerInfo.toTableData(): PlayerInfoTableData {
        return PlayerInfoTableData(
            rowData = PlayerTableLabel.values().map { label ->
                PlayerInfoTableData.RowData.Data(
                    titleRes = label.titleRes,
                    value = LabelHelper.getValueByLabel(label, this)
                )
            }
                .chunked(3)
                .map {
                    PlayerInfoTableData.RowData(it)
                }
        )
    }
}
