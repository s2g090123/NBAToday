package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.home.standing.event.StandingDataEvent
import com.jiachian.nbatoday.compose.screen.home.standing.event.StandingUIEvent
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.state.MutableStandingState
import com.jiachian.nbatoday.compose.screen.home.standing.state.StandingState
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.usecase.team.TeamUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StandingPageViewModel(
    private val teamUseCase: TeamUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val stateImp = MutableStandingState()
    val state: StandingState = stateImp

    private var selectedConference = NBATeam.Conference.values().first()

    init {
        collectTeams(NBATeam.Conference.EAST)
        collectTeams(NBATeam.Conference.WEST)
    }

    private fun collectTeams(conference: NBATeam.Conference) {
        val state = when (conference) {
            NBATeam.Conference.EAST -> stateImp.eastTeamState
            NBATeam.Conference.WEST -> stateImp.westTeamState
        }
        viewModelScope.launch {
            snapshotFlow {
                state.sorting
            }.onStart {
                state.loading = true
            }.flatMapLatest { sorting ->
                teamUseCase
                    .getTeams(conference, sorting)
                    .mapLatest { it.toRowData() }
                    .flowOn(dispatcherProvider.default)
            }.collect { teams ->
                Snapshot.withMutableSnapshot {
                    state.let { state ->
                        state.teams = teams
                        state.loading = false
                    }
                }
            }
        }
    }

    fun onEvent(event: StandingUIEvent) {
        when (event) {
            StandingUIEvent.Refresh -> refreshTeamStats()
            is StandingUIEvent.SelectConference -> {
                selectedConference = event.conference
                stateImp.event = StandingDataEvent.ScrollTo(event.conference.ordinal)
            }
            is StandingUIEvent.UpdateSorting -> {
                when (selectedConference) {
                    NBATeam.Conference.EAST -> stateImp.eastTeamState
                    NBATeam.Conference.WEST -> stateImp.westTeamState
                }.apply {
                    sorting = event.sorting
                }
            }
            StandingUIEvent.EventReceived -> stateImp.event = null
        }
    }

    private fun refreshTeamStats() {
        if (state.refreshing) return
        viewModelScope.launch {
            teamUseCase
                .addTeams()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> stateImp.refreshing = true
                        is Resource.Success -> stateImp.refreshing = false
                        is Resource.Error -> {
                            Snapshot.withMutableSnapshot {
                                stateImp.apply {
                                    refreshing = false
                                    event = StandingDataEvent.Error(resource.error.asStandingError())
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun List<Team>.toRowData(): List<StandingRowData> {
        return map { team ->
            StandingRowData(
                team = team,
                data = StandingLabel.values().map { label ->
                    StandingRowData.Data(
                        value = LabelHelper.getValueByLabel(label, team),
                        width = label.width,
                        align = label.align,
                        sorting = label.sorting,
                    )
                }
            )
        }
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun getSelectedConference() = selectedConference
}
