package com.jiachian.nbatoday.boxscore.ui.main

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.boxscore.domain.BoxScoreUseCase
import com.jiachian.nbatoday.boxscore.domain.error.GetBoxScoreError
import com.jiachian.nbatoday.boxscore.ui.leader.model.BoxScoreLeaderLabel
import com.jiachian.nbatoday.boxscore.ui.leader.model.BoxScoreLeaderRowData
import com.jiachian.nbatoday.boxscore.ui.main.error.asBoxScoreError
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreDataEvent
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreUIEvent
import com.jiachian.nbatoday.boxscore.ui.main.state.BoxScoreState
import com.jiachian.nbatoday.boxscore.ui.main.state.MutableBoxScoreState
import com.jiachian.nbatoday.boxscore.ui.player.model.BoxScorePlayerLabel
import com.jiachian.nbatoday.boxscore.ui.player.model.BoxScorePlayerRowData
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamLabel
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamRowData
import com.jiachian.nbatoday.common.data.WaitForFetching
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.utils.LabelHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    savedStateHandle: SavedStateHandle,
    private val boxScoreUseCase: BoxScoreUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val gameId: String = savedStateHandle[MainRoute.BoxScore.param] ?: ""

    private val stateImp = MutableBoxScoreState()
    val state: BoxScoreState = stateImp

    init {
        updateBoxScore()
        collectBoxScore()
    }

    private fun collectBoxScore() {
        viewModelScope.launch {
            boxScoreUseCase
                .getBoxScore(gameId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            when (resource.error) {
                                GetBoxScoreError.NOT_FOUND -> stateImp.notFound = true
                            }
                        }
                        is Resource.Loading -> Unit
                        is Resource.Success -> {
                            val game = resource.data
                            withContext(dispatcherProvider.default) {
                                val periods = game.boxScore.homeTeam.periods.map { it.periodLabel }
                                val homePlayersRowData = game.boxScore.homeTeam.players.map { it.toRowData() }
                                val awayPlayersRowData = game.boxScore.awayTeam.players.map { it.toRowData() }
                                val teamRowData = game.boxScore.toTeamRowData()
                                val leaderRowData = game.boxScore.toLeaderRowData(
                                    game.game.homeLeaderId,
                                    game.game.awayLeaderId
                                )
                                Snapshot.withMutableSnapshot {
                                    stateImp.apply {
                                        info.let { info ->
                                            info.boxScore = game.boxScore
                                            info.dateString = game.boxScore.gameDate
                                            info.periods = periods
                                        }
                                        player.let { player ->
                                            player.homePlayers = homePlayersRowData
                                            player.awayPlayers = awayPlayersRowData
                                        }
                                        team.let { team ->
                                            team.homeTeam = game.boxScore.homeTeam.team
                                            team.awayTeam = game.boxScore.awayTeam.team
                                            team.data = teamRowData
                                        }
                                        leader.let { leader ->
                                            leader.homePlayerId = game.game.homeLeaderId
                                            leader.awayPlayerId = game.game.awayLeaderId
                                            leader.data = leaderRowData
                                        }
                                        notFound = false
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun updateBoxScore() {
        viewModelScope.launch {
            boxScoreUseCase
                .addBoxScore(gameId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            Snapshot.withMutableSnapshot {
                                stateImp.apply {
                                    event = BoxScoreDataEvent.Error(resource.error.asBoxScoreError())
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

    fun onEvent(event: BoxScoreUIEvent) {
        when (event) {
            BoxScoreUIEvent.EventReceived -> stateImp.event = null
        }
    }

    private fun BoxScore.BoxScoreTeam.Player.toRowData(): BoxScorePlayerRowData {
        return BoxScorePlayerRowData(
            player = this,
            data = BoxScorePlayerLabel.values().map { label ->
                BoxScorePlayerRowData.Data(
                    value = LabelHelper.getValueByLabel(label, statistics),
                    width = label.width,
                    align = label.align,
                )
            }
        )
    }

    private fun BoxScore.toTeamRowData(): List<BoxScoreTeamRowData> {
        return BoxScoreTeamLabel.values().map { label ->
            BoxScoreTeamRowData(
                label = label,
                home = LabelHelper.getValueByLabel(label, homeTeam.statistics),
                away = LabelHelper.getValueByLabel(label, awayTeam.statistics),
            )
        }
    }

    private fun BoxScore.toLeaderRowData(homeLeader: Int, awayLeader: Int): List<BoxScoreLeaderRowData> {
        return BoxScoreLeaderLabel.values().map { label ->
            BoxScoreLeaderRowData(
                label = label,
                home = LabelHelper.getValueByLabel(label, homeTeam.players.first { it.playerId == homeLeader }),
                away = LabelHelper.getValueByLabel(label, awayTeam.players.first { it.playerId == awayLeader }),
            )
        }
    }
}
