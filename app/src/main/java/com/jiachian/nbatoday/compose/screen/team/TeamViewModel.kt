package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.team.event.TeamDataEvent
import com.jiachian.nbatoday.compose.screen.team.event.TeamUIEvent
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.state.MutableTeamState
import com.jiachian.nbatoday.compose.screen.team.state.TeamState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.toGameCardState
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.team.GetTeamAndPlayersError
import com.jiachian.nbatoday.usecase.team.TeamUseCase
import com.jiachian.nbatoday.usecase.team.UpdateTeamInfoError
import com.jiachian.nbatoday.usecase.user.GetUser
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
