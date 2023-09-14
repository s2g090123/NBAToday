package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.player.PlayerCareer.PlayerCareerStats.Stats
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerInfoViewModel(
    private val playerId: Int,
    private val repository: BaseRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val playerCareer = repository.getPlayerCareer(playerId)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val labelToEvaluationAccessor = mapOf(
        "GP" to { stats: Stats -> stats.gamePlayed.toString() },
        "W" to { stats: Stats -> stats.win.toString() },
        "L" to { stats: Stats -> stats.lose.toString() },
        "WIN%" to { stats: Stats -> stats.winPercentage.toString() },
        "PTS" to { stats: Stats -> (stats.points.toDouble() / stats.gamePlayed).toString() },
        "FGM" to { stats: Stats -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).toString() },
        "FGA" to { stats: Stats -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FG%" to { stats: Stats -> stats.fieldGoalsPercentage.toString() },
        "3PM" to { stats: Stats -> (stats.threePointersMade.toDouble() / stats.gamePlayed).toString() },
        "3PA" to { stats: Stats -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).toString() },
        "3P%" to { stats: Stats -> stats.threePointersPercentage.toString() },
        "FTM" to { stats: Stats -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).toString() },
        "FTA" to { stats: Stats -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).toString() },
        "FT%" to { stats: Stats -> stats.freeThrowsPercentage.toString() },
        "OREB" to { stats: Stats -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).toString() },
        "DREB" to { stats: Stats -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).toString() },
        "REB" to { stats: Stats -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).toString() },
        "AST" to { stats: Stats -> (stats.assists.toDouble() / stats.gamePlayed).toString() },
        "TOV" to { stats: Stats -> (stats.turnovers.toDouble() / stats.gamePlayed).toString() },
        "STL" to { stats: Stats -> (stats.steals.toDouble() / stats.gamePlayed).toString() },
        "BLK" to { stats: Stats -> (stats.blocks.toDouble() / stats.gamePlayed).toString() },
        "PF" to { stats: Stats -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).toString() },
        "+/-" to { stats: Stats -> stats.plusMinus.toString() }
    )

    val statsLabels = derivedStateOf {
        listOf(
            CareerStatsLabel(40.dp, "GP", TextAlign.End, CareerStatsSort.GP),
            CareerStatsLabel(40.dp, "W", TextAlign.End, CareerStatsSort.W),
            CareerStatsLabel(40.dp, "L", TextAlign.End, CareerStatsSort.L),
            CareerStatsLabel(64.dp, "WIN%", TextAlign.End, CareerStatsSort.WINP),
            CareerStatsLabel(64.dp, "PTS", TextAlign.End, CareerStatsSort.PTS),
            CareerStatsLabel(64.dp, "FGM", TextAlign.End, CareerStatsSort.FGM),
            CareerStatsLabel(64.dp, "FGA", TextAlign.End, CareerStatsSort.FGA),
            CareerStatsLabel(64.dp, "FG%", TextAlign.End, CareerStatsSort.FGP),
            CareerStatsLabel(64.dp, "3PM", TextAlign.End, CareerStatsSort.PM3),
            CareerStatsLabel(64.dp, "3PA", TextAlign.End, CareerStatsSort.PA3),
            CareerStatsLabel(64.dp, "3P%", TextAlign.End, CareerStatsSort.PP3),
            CareerStatsLabel(64.dp, "FTM", TextAlign.End, CareerStatsSort.FTM),
            CareerStatsLabel(64.dp, "FTA", TextAlign.End, CareerStatsSort.FTA),
            CareerStatsLabel(64.dp, "FT%", TextAlign.End, CareerStatsSort.FTP),
            CareerStatsLabel(48.dp, "OREB", TextAlign.End, CareerStatsSort.OREB),
            CareerStatsLabel(48.dp, "DREB", TextAlign.End, CareerStatsSort.DREB),
            CareerStatsLabel(48.dp, "REB", TextAlign.End, CareerStatsSort.REB),
            CareerStatsLabel(48.dp, "AST", TextAlign.End, CareerStatsSort.AST),
            CareerStatsLabel(48.dp, "TOV", TextAlign.End, CareerStatsSort.TOV),
            CareerStatsLabel(48.dp, "STL", TextAlign.End, CareerStatsSort.STL),
            CareerStatsLabel(48.dp, "BLK", TextAlign.End, CareerStatsSort.BLK),
            CareerStatsLabel(48.dp, "PF", TextAlign.End, CareerStatsSort.PF),
            CareerStatsLabel(48.dp, "+/-", TextAlign.End, CareerStatsSort.PLUSMINUS)
        )
    }

    private val statsSortImp = MutableStateFlow(CareerStatsSort.TIME_FRAME)
    val statsSort = statsSortImp.asStateFlow()

    val careerStats = combine(
        playerCareer,
        statsSort
    ) { stats, sort ->
        val careerStats = stats?.stats?.careerStats ?: emptyList()
        when (sort) {
            CareerStatsSort.TIME_FRAME -> careerStats.sortedWith(
                compareByDescending {
                    it.timeFrame
                }
            )
            CareerStatsSort.GP -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.W -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.win
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.L -> careerStats.sortedWith(
                compareBy<Stats> {
                    it.lose
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.WINP -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.winPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PTS -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.points.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FGM -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.fieldGoalsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FGA -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.fieldGoalsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FGP -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.fieldGoalsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PM3 -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.threePointersMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PA3 -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.threePointersAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PP3 -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.threePointersPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FTM -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.freeThrowsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FTA -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.freeThrowsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.FTP -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.freeThrowsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.OREB -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.reboundsOffensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.DREB -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.reboundsDefensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.REB -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.reboundsTotal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.AST -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.assists.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.TOV -> careerStats.sortedWith(
                compareBy<Stats> {
                    it.turnovers.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.STL -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.steals.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.BLK -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.blocks.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PF -> careerStats.sortedWith(
                compareBy<Stats> {
                    it.foulsPersonal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )
            CareerStatsSort.PLUSMINUS -> careerStats.sortedWith(
                compareByDescending<Stats> {
                    it.plusMinus
                }.thenByDescending {
                    it.winPercentage
                }
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    init {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.refreshPlayerStats(playerId)
            }
            isRefreshingImp.value = false
        }
    }

    fun updateStatsSort(sort: CareerStatsSort) {
        statsSortImp.value = sort
    }

    fun getEvaluationTextByLabel(label: CareerStatsLabel, stats: Stats): String {
        val labelText = label.text
        val accessor = labelToEvaluationAccessor[labelText] ?: return ""
        return accessor(stats)
    }
}
