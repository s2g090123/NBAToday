package com.jiachian.nbatoday.compose.screen.home.standing

import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    }
    private val westRowData = westTeams.map { teams ->
        teams.toRowData()
    }

    val sortedEastRowData = combine(
        eastRowData,
        eastSorting
    ) { rowData, sorting ->
        rowData.sortedWith(sorting)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
    val sortedWestRowData = combine(
        westRowData,
        westSorting
    ) { rowData, sorting ->
        rowData.sortedWith(sorting)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    private val selectedConference = MutableStateFlow(NBATeam.Conference.EAST)

    fun updateTeamStats() {
        if (isRefreshing.value) return
        coroutineScope.launch(dispatcherProvider.io) {
            isRefreshingImp.value = true
            repository.updateTeamStats()
            isRefreshingImp.value = false
        }
    }

    fun selectConference(conference: NBATeam.Conference) {
        selectedConference.value = conference
    }

    fun updateSorting(sorting: StandingSorting) {
        when (selectedConference.value) {
            NBATeam.Conference.EAST -> eastSortingImp.value = sorting
            NBATeam.Conference.WEST -> westSortingImp.value = sorting
        }
    }

    fun onClickTeam(team: NBATeam) {
        navigationController.navigateToTeam(team.teamId)
    }

    private suspend fun List<StandingRowData>.sortedWith(sorting: StandingSorting): List<StandingRowData> {
        val comparator = when (sorting) {
            StandingSorting.GP -> compareByDescending { it.team.gamePlayed }
            StandingSorting.W -> compareByDescending { it.team.win }
            StandingSorting.L -> compareBy { it.team.lose }
            StandingSorting.WINP -> compareByDescending { it.team.winPercentage }
            StandingSorting.PTS -> compareByDescending { it.team.pointsAverage }
            StandingSorting.FGM -> compareByDescending { it.team.fieldGoalsMadeAverage }
            StandingSorting.FGA -> compareByDescending { it.team.fieldGoalsAttemptedAverage }
            StandingSorting.FGP -> compareByDescending { it.team.fieldGoalsPercentage }
            StandingSorting.PM3 -> compareByDescending { it.team.threePointersMadeAverage }
            StandingSorting.PA3 -> compareByDescending { it.team.threePointersAttemptedAverage }
            StandingSorting.PP3 -> compareByDescending { it.team.threePointersPercentage }
            StandingSorting.FTM -> compareByDescending { it.team.freeThrowsMadeAverage }
            StandingSorting.FTA -> compareByDescending { it.team.freeThrowsAttemptedAverage }
            StandingSorting.FTP -> compareByDescending { it.team.freeThrowsPercentage }
            StandingSorting.OREB -> compareByDescending { it.team.reboundsOffensiveAverage }
            StandingSorting.DREB -> compareByDescending { it.team.reboundsDefensiveAverage }
            StandingSorting.REB -> compareByDescending { it.team.reboundsTotalAverage }
            StandingSorting.AST -> compareByDescending { it.team.assistsAverage }
            StandingSorting.TOV -> compareBy { it.team.turnoversAverage }
            StandingSorting.STL -> compareByDescending { it.team.stealsAverage }
            StandingSorting.BLK -> compareByDescending { it.team.blocksAverage }
            StandingSorting.PF -> compareBy<StandingRowData> { it.team.foulsPersonalAverage }
        }.thenByDescending { it.team.winPercentage }
        return withContext(dispatcherProvider.io) {
            sortedWith(comparator)
        }
    }

    private suspend fun List<Team>.toRowData(): List<StandingRowData> {
        return withContext(dispatcherProvider.io) {
            map { team ->
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
}
