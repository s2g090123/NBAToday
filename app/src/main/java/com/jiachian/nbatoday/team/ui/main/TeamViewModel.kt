package com.jiachian.nbatoday.team.ui.main

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.game.data.model.toGameCardState
import com.jiachian.nbatoday.game.domain.GameUseCase
import com.jiachian.nbatoday.home.user.domain.GetUser
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.team.domain.TeamUseCase
import com.jiachian.nbatoday.team.domain.error.GetTeamAndPlayersError
import com.jiachian.nbatoday.team.domain.error.UpdateTeamInfoError
import com.jiachian.nbatoday.team.ui.main.error.asTeamError
import com.jiachian.nbatoday.team.ui.main.event.TeamDataEvent
import com.jiachian.nbatoday.team.ui.main.event.TeamUIEvent
import com.jiachian.nbatoday.team.ui.main.state.MutableTeamState
import com.jiachian.nbatoday.team.ui.main.state.TeamState
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerLabel
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerRowData
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.LabelHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class TeamViewModel(
    savedStateHandle: SavedStateHandle,
    private val teamUseCase: TeamUseCase,
    private val gameUseCase: GameUseCase,
    getUser: GetUser,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val teamId: Int = savedStateHandle.get<String>(MainRoute.Team.param)?.toIntOrNull() ?: throw Exception("teamId is null.")

    private val stateImp = MutableTeamState()
    val state: TeamState = stateImp

    private val user = getUser()

    val colors = NBATeam.getTeamById(teamId).colors

    init {
        updateTeam()
        collectTeamAndPlayers()
        collectTeamRank()
        collectGames()
    }

    private fun updateTeam() {
        viewModelScope.launch {
            teamUseCase
                .updateTeamInfo(teamId)
                .collect {
                    when (it) {
                        is Resource.Error -> {
                            when (it.error) {
                                UpdateTeamInfoError.UPDATE_TEAMS_FAILED,
                                UpdateTeamInfoError.UPDATE_PLAYERS_FAILED -> {
                                    Snapshot.withMutableSnapshot {
                                        stateImp.apply {
                                            event = TeamDataEvent.Error(it.error.asTeamError())
                                            loading = false
                                        }
                                    }
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

    private fun collectTeamAndPlayers() {
        viewModelScope.launch {
            snapshotFlow { state.players.sorting }
                .flatMapLatest { sorting ->
                    teamUseCase
                        .getTeamAndPlayers(teamId, sorting)
                        .flowOn(dispatcherProvider.default)
                }
                .distinctUntilChanged()
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            when (resource.error) {
                                GetTeamAndPlayersError.TEAM_NOT_FOUND -> {
                                    Snapshot.withMutableSnapshot {
                                        stateImp.apply {
                                            event = TeamDataEvent.Error(resource.error.asTeamError())
                                            notFound = true
                                        }
                                    }
                                }
                            }
                        }
                        is Resource.Loading -> Unit
                        is Resource.Success -> {
                            val playerRowData = withContext(dispatcherProvider.default) {
                                resource.data.teamPlayers.map { it.toRowData() }
                            }
                            Snapshot.withMutableSnapshot {
                                stateImp.apply {
                                    info.team = resource.data.team
                                    players.players = playerRowData
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun collectTeamRank() {
        viewModelScope.launch {
            teamUseCase
                .getTeamRank(teamId, NBATeam.getTeamById(teamId).conference)
                .collect {
                    stateImp.info.rank = it
                }
        }
    }

    private fun collectGames() {
        val time = DateUtils.getCalendar().run {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
            timeInMillis
        }
        viewModelScope.launch {
            gameUseCase
                .getGamesBefore(teamId, time)
                .mapLatest {
                    it.toGameCardState(user)
                }
                .flowOn(dispatcherProvider.default)
                .collect {
                    stateImp.games.previous = it
                }
        }
        viewModelScope.launch {
            gameUseCase
                .getGamesAfter(teamId, time)
                .mapLatest {
                    it.toGameCardState(user)
                }
                .flowOn(dispatcherProvider.default)
                .collect {
                    stateImp.games.next = it
                }
        }
    }

    fun onEvent(event: TeamUIEvent) {
        when (event) {
            TeamUIEvent.EventReceived -> stateImp.event = null
            is TeamUIEvent.Sort -> stateImp.players.sorting = event.sorting
        }
    }

    private fun TeamPlayer.toRowData(): TeamPlayerRowData {
        return TeamPlayerRowData(
            player = this,
            data = TeamPlayerLabel.values().map { label ->
                TeamPlayerRowData.Data(
                    value = LabelHelper.getValueByLabel(label, this),
                    width = label.width,
                    sorting = label.sorting,
                )
            }
        )
    }
}
