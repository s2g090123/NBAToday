package com.jiachian.nbatoday.compose.screen.home.standing

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StandingPageViewModel(
    private val repository: TeamRepository,
    private val navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    private val labelToEvaluationAccessor = mapOf(
        "GP" to { stats: Team -> stats.gamePlayed.toString() },
        "W" to { stats: Team -> stats.win.toString() },
        "L" to { stats: Team -> stats.lose.toString() },
        "WIN%" to { stats: Team -> stats.winPercentage.toString() },
        "PTS" to { stats: Team -> (stats.points.toDouble() / stats.gamePlayed).toString() },
        "FGM" to { stats: Team -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).toString() },
        "FGA" to { stats: Team -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FG%" to { stats: Team -> stats.fieldGoalsPercentage.toString() },
        "3PM" to { stats: Team -> (stats.threePointersMade.toDouble() / stats.gamePlayed).toString() },
        "3PA" to { stats: Team -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).toString() },
        "3P%" to { stats: Team -> stats.threePointersPercentage.toString() },
        "FTM" to { stats: Team -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).toString() },
        "FTA" to { stats: Team -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FT%" to { stats: Team -> stats.freeThrowsPercentage.toString() },
        "OREB" to { stats: Team -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).toString() },
        "DREB" to { stats: Team -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).toString() },
        "REB" to { stats: Team -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).toString() },
        "AST" to { stats: Team -> (stats.assists.toDouble() / stats.gamePlayed).toString() },
        "TOV" to { stats: Team -> (stats.turnovers.toDouble() / stats.gamePlayed).toString() },
        "STL" to { stats: Team -> (stats.steals.toDouble() / stats.gamePlayed).toString() },
        "BLK" to { stats: Team -> (stats.blocks.toDouble() / stats.gamePlayed).toString() },
        "PF" to { stats: Team -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).toString() }
    )
    private val standingSortImp = MutableStateFlow(StandingSort.WINP)
    val standingSort = standingSortImp.asStateFlow()
    val team = combine(
        repository.getTeams(),
        standingSort
    ) { teamStats, sort ->
        when (sort) {
            StandingSort.GP -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.W -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.win
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.L -> teamStats.sortedWith(
                compareBy<Team> {
                    it.lose
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.WINP -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.winPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PTS -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.points.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGM -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.fieldGoalsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGA -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.fieldGoalsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FGP -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.fieldGoalsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PM3 -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.threePointersMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PA3 -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.threePointersAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PP3 -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.threePointersPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTM -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.freeThrowsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTA -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.freeThrowsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.FTP -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.freeThrowsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.OREB -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.reboundsOffensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.DREB -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.reboundsDefensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.REB -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.reboundsTotal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.AST -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.assists.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.TOV -> teamStats.sortedWith(
                compareBy<Team> {
                    it.turnovers.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.STL -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.steals.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.BLK -> teamStats.sortedWith(
                compareByDescending<Team> {
                    it.blocks.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            StandingSort.PF -> teamStats.sortedWith(
                compareBy<Team> {
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
                repository.updateTeamStats()
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
        navigationController.navigateToTeam(team.teamId)
    }

    fun getEvaluationTextByLabel(label: StandingLabel, stats: Team): String {
        val labelText = label.text
        val accessor = labelToEvaluationAccessor[labelText] ?: return ""
        return accessor(stats)
    }

    override fun close() {
        coroutineScope.cancel()
    }
}
