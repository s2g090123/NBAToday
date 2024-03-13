package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingRowData
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
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

/**
 * ViewModel for handling business logic related to [StandingPage].
 *
 * @property repository The repository for interacting with [Team].
 * @property navigationController The controller for navigation within the app.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class StandingPageViewModel(
    private val repository: TeamRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val eastTeams = repository.getTeams(NBATeam.Conference.EAST)
    private val westTeams = repository.getTeams(NBATeam.Conference.WEST)

    // list of available labels for the standing page
    val labels = StandingLabel.values()
    val conferences = NBATeam.Conference.values()

    // the sorting criteria for the Eastern and Western conferences
    private val eastSortingImp = MutableStateFlow(StandingSorting.WINP)
    private val westSortingImp = MutableStateFlow(StandingSorting.WINP)
    val eastSorting = eastSortingImp.asStateFlow()
    val westSorting = westSortingImp.asStateFlow()

    // the row data for the Eastern and Western conferences
    private val eastRowData = eastTeams.map { teams ->
        teams.toRowData()
    }.flowOn(dispatcherProvider.io)
    private val westRowData = westTeams.map { teams ->
        teams.toRowData()
    }.flowOn(dispatcherProvider.io)

    // the sorted row data for the Eastern and Western conference.
    val sortedEastRowDataState = combine(
        eastRowData,
        eastSorting
    ) { rowData, sorting ->
        UIState.Loaded(rowData.sortedWith(sorting))
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(viewModelScope, SharingStarted.Lazily, UIState.Loading())
    val sortedWestRowDataState = combine(
        westRowData,
        westSorting
    ) { rowData, sorting ->
        UIState.Loaded(rowData.sortedWith(sorting))
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(viewModelScope, SharingStarted.Lazily, UIState.Loading())

    // the refreshing status of the standing page
    private val refreshingImp = MutableStateFlow(false)
    val refreshing = refreshingImp.asStateFlow()

    private var selectedConference = NBATeam.Conference.EAST

    /**
     * Updates team statistics by fetching the latest data from the repository.
     */
    fun updateTeamStats() {
        if (refreshing.value) return
        viewModelScope.launch(dispatcherProvider.io) {
            refreshingImp.value = true
            repository.insertTeams()
            refreshingImp.value = false
        }
    }

    fun selectConference(conference: NBATeam.Conference) {
        selectedConference = conference
    }

    /**
     * Updates the sorting criteria based on the selected conference.
     *
     * @param sorting The selected sorting criteria.
     */
    fun updateSorting(sorting: StandingSorting) {
        when (selectedConference) {
            NBATeam.Conference.EAST -> eastSortingImp.value = sorting
            NBATeam.Conference.WEST -> westSortingImp.value = sorting
        }
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
        }
            .thenByDescending { it.team.winPercentage }
            .thenByDescending { it.team.gamePlayed }
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

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun getSelectedConference() = selectedConference
}
