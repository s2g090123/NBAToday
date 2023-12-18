package com.jiachian.nbatoday.compose.screen.home.standing

import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StandingPageViewModel(
    private val repository: TeamRepository,
    private val navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) {
    private val eastTeams = repository.getTeams(NBATeam.Conference.EAST)
    private val westTeams = repository.getTeams(NBATeam.Conference.WEST)

    val labels = StandingLabel.values()
    val conferences = NBATeam.Conference.values()

    private val eastSortingImp = MutableStateFlow(StandingSorting.WINP)
    private val westSortingImp = MutableStateFlow(StandingSorting.WINP)
    val eastSorting = eastSortingImp.asStateFlow()
    val westSorting = westSortingImp.asStateFlow()

    private val eastRowData = eastTeams.map { teams ->
        teams.toRowData()
    }.flowOn(dispatcherProvider.io)
    private val westRowData = westTeams.map { teams ->
        teams.toRowData()
    }.flowOn(dispatcherProvider.io)

    val sortedEastRowDataState = combine(
        eastRowData,
        eastSorting
    ) { rowData, sorting ->
        UIState.Loaded(rowData.sortedWith(sorting))
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())
    val sortedWestRowDataState = combine(
        westRowData,
        westSorting
    ) { rowData, sorting ->
        UIState.Loaded(rowData.sortedWith(sorting))
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val refreshingImp = MutableStateFlow(false)
    val refreshing = refreshingImp.asStateFlow()

    private var selectedConference = NBATeam.Conference.EAST

    fun updateTeamStats() {
        if (refreshing.value) return
        coroutineScope.launch(dispatcherProvider.io) {
            refreshingImp.value = true
            repository.insertTeams()
            refreshingImp.value = false
        }
    }

    fun selectConference(conference: NBATeam.Conference) {
        selectedConference = conference
    }

    fun updateSorting(sorting: StandingSorting) {
        when (selectedConference) {
            NBATeam.Conference.EAST -> eastSortingImp.value = sorting
            NBATeam.Conference.WEST -> westSortingImp.value = sorting
        }
    }

    fun onClickTeam(team: NBATeam) {
        navigationController.navigateToTeam(team.teamId)
    }

    private fun List<StandingRowData>.sortedWith(sorting: StandingSorting): List<StandingRowData> {
        val comparator = when (sorting) {
            StandingSorting.GP -> compareByDescending { it.team.gamePlayed }
            StandingSorting.W -> compareByDescending { it.team.win }
            StandingSorting.L -> compareBy { it.team.lose }
            StandingSorting.WINP -> compareByDescending { it.team.winPercentage }
            StandingSorting.PTS -> compareByDescending { it.team.pointsAverage }
            StandingSorting.FGP -> compareByDescending { it.team.fieldGoalsPercentage }
            StandingSorting.PP3 -> compareByDescending { it.team.threePointersPercentage }
            StandingSorting.FTP -> compareByDescending { it.team.freeThrowsPercentage }
            StandingSorting.OREB -> compareByDescending { it.team.reboundsOffensiveAverage }
            StandingSorting.DREB -> compareByDescending { it.team.reboundsDefensiveAverage }
            StandingSorting.AST -> compareByDescending { it.team.assistsAverage }
            StandingSorting.TOV -> compareBy { it.team.turnoversAverage }
            StandingSorting.STL -> compareByDescending { it.team.stealsAverage }
            StandingSorting.BLK -> compareByDescending<StandingRowData> { it.team.blocksAverage }
        }.thenByDescending { it.team.winPercentage }
        return sortedWith(comparator)
    }

    private fun List<Team>.toRowData(): List<StandingRowData> {
        return map { team ->
            StandingRowData(
                team = team,
                data = labels.map { label ->
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
}
