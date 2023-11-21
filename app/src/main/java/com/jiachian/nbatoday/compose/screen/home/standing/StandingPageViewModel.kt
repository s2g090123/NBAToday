package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.repository.team.TeamRepository
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StandingPageViewModel(
    private val repository: TeamRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {

    private val labelToEvaluationAccessor = mapOf(
        "GP" to { stats: TeamStats -> stats.gamePlayed.toString() },
        "W" to { stats: TeamStats -> stats.win.toString() },
        "L" to { stats: TeamStats -> stats.lose.toString() },
        "WIN%" to { stats: TeamStats -> stats.winPercentage.toString() },
        "PTS" to { stats: TeamStats -> (stats.points.toDouble() / stats.gamePlayed).toString() },
        "FGM" to { stats: TeamStats -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).toString() },
        "FGA" to { stats: TeamStats -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FG%" to { stats: TeamStats -> stats.fieldGoalsPercentage.toString() },
        "3PM" to { stats: TeamStats -> (stats.threePointersMade.toDouble() / stats.gamePlayed).toString() },
        "3PA" to { stats: TeamStats -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).toString() },
        "3P%" to { stats: TeamStats -> stats.threePointersPercentage.toString() },
        "FTM" to { stats: TeamStats -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).toString() },
        "FTA" to { stats: TeamStats -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FT%" to { stats: TeamStats -> stats.freeThrowsPercentage.toString() },
        "OREB" to { stats: TeamStats -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).toString() },
        "DREB" to { stats: TeamStats -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).toString() },
        "REB" to { stats: TeamStats -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).toString() },
        "AST" to { stats: TeamStats -> (stats.assists.toDouble() / stats.gamePlayed).toString() },
        "TOV" to { stats: TeamStats -> (stats.turnovers.toDouble() / stats.gamePlayed).toString() },
        "STL" to { stats: TeamStats -> (stats.steals.toDouble() / stats.gamePlayed).toString() },
        "BLK" to { stats: TeamStats -> (stats.blocks.toDouble() / stats.gamePlayed).toString() },
        "PF" to { stats: TeamStats -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).toString() }
    )
    private val standingSortImp = MutableStateFlow(StandingSort.WINP)
    val standingSort = standingSortImp.asStateFlow()
    val teamStats = combine(
        repository.getTeamStats(),
        standingSort
    ) { teamStats, sort ->
        when (sort) {
            StandingSort.GP -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.W -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.win
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.L -> teamStats.sortedWith(
                compareBy<TeamStats> {
                    it.lose
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.WINP -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.winPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PTS -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.points.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGM -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.fieldGoalsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGA -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.fieldGoalsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGP -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.fieldGoalsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PM3 -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.threePointersMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PA3 -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.threePointersAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PP3 -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.threePointersPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTM -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.freeThrowsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTA -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.freeThrowsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTP -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.freeThrowsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.OREB -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.reboundsOffensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.DREB -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.reboundsDefensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.REB -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.reboundsTotal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.AST -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.assists.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.TOV -> teamStats.sortedWith(
                compareBy<TeamStats> {
                    it.turnovers.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.STL -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.steals.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.BLK -> teamStats.sortedWith(
                compareByDescending<TeamStats> {
                    it.blocks.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PF -> teamStats.sortedWith(
                compareBy<TeamStats> {
                    it.foulsPersonal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
        }.groupBy {
            it.teamConference
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, mapOf())
    private val isRefreshingTeamStatsImp = MutableStateFlow(false)
    val isRefreshingTeamStats = isRefreshingTeamStatsImp.asStateFlow()
    private val selectConferenceImp = MutableStateFlow(NBATeam.Conference.EAST)
    val selectedConference = selectConferenceImp.asStateFlow()
    val standingLabel = derivedStateOf {
        listOf(
            StandingLabel(40.dp, "GP", TextAlign.End, StandingSort.GP),
            StandingLabel(40.dp, "W", TextAlign.End, StandingSort.W),
            StandingLabel(40.dp, "L", TextAlign.End, StandingSort.L),
            StandingLabel(64.dp, "WIN%", TextAlign.End, StandingSort.WINP),
            StandingLabel(64.dp, "PTS", TextAlign.End, StandingSort.PTS),
            StandingLabel(64.dp, "FGM", TextAlign.End, StandingSort.FGM),
            StandingLabel(64.dp, "FGA", TextAlign.End, StandingSort.FGA),
            StandingLabel(64.dp, "FG%", TextAlign.End, StandingSort.FGP),
            StandingLabel(64.dp, "3PM", TextAlign.End, StandingSort.PM3),
            StandingLabel(64.dp, "3PA", TextAlign.End, StandingSort.PA3),
            StandingLabel(64.dp, "3P%", TextAlign.End, StandingSort.PP3),
            StandingLabel(64.dp, "FTM", TextAlign.End, StandingSort.FTM),
            StandingLabel(64.dp, "FTA", TextAlign.End, StandingSort.FTA),
            StandingLabel(64.dp, "FT%", TextAlign.End, StandingSort.FTP),
            StandingLabel(48.dp, "OREB", TextAlign.End, StandingSort.OREB),
            StandingLabel(48.dp, "DREB", TextAlign.End, StandingSort.DREB),
            StandingLabel(48.dp, "REB", TextAlign.End, StandingSort.REB),
            StandingLabel(48.dp, "AST", TextAlign.End, StandingSort.AST),
            StandingLabel(48.dp, "TOV", TextAlign.End, StandingSort.TOV),
            StandingLabel(48.dp, "STL", TextAlign.End, StandingSort.STL),
            StandingLabel(48.dp, "BLK", TextAlign.End, StandingSort.BLK),
            StandingLabel(48.dp, "PF", TextAlign.End, StandingSort.PF)
        )
    }

    fun updateTeamStats() {
        if (isRefreshingTeamStats.value) return
        coroutineScope.launch {
            isRefreshingTeamStatsImp.value = true
            withContext(dispatcherProvider.io) {
                repository.refreshTeamStats(null)
            }
            isRefreshingTeamStatsImp.value = false
        }
    }

    fun selectConference(conference: NBATeam.Conference) {
        selectConferenceImp.value = conference
    }

    fun updateStandingSort(sorting: StandingSort) {
        standingSortImp.value = sorting
    }

    fun openTeamStats(team: NBATeam) {
        screenStateHelper.openScreen(NbaScreenState.Team(team))
    }

    fun getEvaluationTextByLabel(label: StandingLabel, stats: TeamStats): String {
        val labelText = label.text
        val accessor = labelToEvaluationAccessor[labelText] ?: return ""
        return accessor(stats)
    }
}
