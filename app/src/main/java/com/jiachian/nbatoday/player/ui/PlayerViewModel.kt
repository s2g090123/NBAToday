package com.jiachian.nbatoday.player.ui

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.data.WaitForFetching
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.player.domain.PlayerUseCase
import com.jiachian.nbatoday.player.domain.error.GetPlayerError
import com.jiachian.nbatoday.player.ui.error.asPlayerError
import com.jiachian.nbatoday.player.ui.event.PlayerDataEvent
import com.jiachian.nbatoday.player.ui.event.PlayerUIEvent
import com.jiachian.nbatoday.player.ui.model.PlayerInfoTableData
import com.jiachian.nbatoday.player.ui.model.PlayerStatsLabel
import com.jiachian.nbatoday.player.ui.model.PlayerStatsRowData
import com.jiachian.nbatoday.player.ui.model.PlayerTableLabel
import com.jiachian.nbatoday.player.ui.state.MutablePlayerState
import com.jiachian.nbatoday.player.ui.state.PlayerState
import com.jiachian.nbatoday.utils.LabelHelper
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
    companion object {
        private const val COUNT_PER_ROW = 3
    }

    private val playerId: Int = savedStateHandle.get<String>(MainRoute.Player.param)?.toIntOrNull() ?: -1

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
            }.collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        when (resource.error) {
                            GetPlayerError.NOT_FOUND -> stateImp.notFound = true
                        }
                    }
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        val player = resource.data
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
                            delay(WaitForFetching) // delay for fetching new data
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
                .chunked(COUNT_PER_ROW)
                .map {
                    PlayerInfoTableData.RowData(it)
                }
        )
    }
}
