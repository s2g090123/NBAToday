package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.event.BoxScoreDataEvent
import com.jiachian.nbatoday.compose.screen.score.event.BoxScoreUIEvent
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.score.state.BoxScoreState
import com.jiachian.nbatoday.compose.screen.score.state.MutableBoxScoreState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.boxscore.BoxScoreUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    savedStateHandle: SavedStateHandle,
    private val boxScoreUseCase: BoxScoreUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val gameId: String = savedStateHandle[MainRoute.BoxScore.param] ?: throw Exception("gameId is null.")

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
                .collectLatest { game ->
                    if (game == null) {
                        stateImp.notFound = true
                    } else {
                        withContext(dispatcherProvider.default) {
                            val periods = game.boxScore.homeTeam.periods.map { it.periodLabel }
                            val homePlayersRowData = game.boxScore.homeTeam.players.map { it.toRowData() }
                            val awayPlayersRowData = game.boxScore.awayTeam.players.map { it.toRowData() }
                            val teamRowData = game.boxScore.toTeamRowData()
                            val leaderRowData = game.boxScore.toLeaderRowData(game.game.homeLeaderId, game.game.awayLeaderId)
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
                            delay(300) // delay for fetching new data
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
