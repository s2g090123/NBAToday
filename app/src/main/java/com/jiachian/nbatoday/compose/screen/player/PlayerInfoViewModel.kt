package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.player.PlayerCareer.PlayerCareerStats.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CareerStatsLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: CareerStatsSort?
)

class PlayerInfoViewModel(
    private val playerId: Int,
    private val repository: BaseRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
) : ComposeViewModel() {

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val playerCareer = repository.getPlayerCareer(playerId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val statsLabels = derivedStateOf {
        listOf(
            CareerStatsLabel(80.dp, "By Year", TextAlign.Start, CareerStatsSort.TIME_FRAME),
            CareerStatsLabel(64.dp, "Team", TextAlign.End, null),
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
            CareerStatsSort.TIME_FRAME -> careerStats.sortedWith(compareByDescending { it.timeFrame })
            CareerStatsSort.GP -> careerStats.sortedWith(compareByDescending<Stats> { it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.W -> careerStats.sortedWith(compareByDescending<Stats> { it.win }.thenByDescending { it.winPercentage })
            CareerStatsSort.L -> careerStats.sortedWith(compareBy<Stats> { it.lose }.thenByDescending { it.winPercentage })
            CareerStatsSort.WINP -> careerStats.sortedWith(compareByDescending<Stats> { it.winPercentage }.thenByDescending { it.winPercentage })
            CareerStatsSort.PTS -> careerStats.sortedWith(compareByDescending<Stats> { it.points.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.FGM -> careerStats.sortedWith(compareByDescending<Stats> { it.fieldGoalsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.FGA -> careerStats.sortedWith(compareByDescending<Stats> { it.fieldGoalsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.FGP -> careerStats.sortedWith(compareByDescending<Stats> { it.fieldGoalsPercentage }.thenByDescending { it.winPercentage })
            CareerStatsSort.PM3 -> careerStats.sortedWith(compareByDescending<Stats> { it.threePointersMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.PA3 -> careerStats.sortedWith(compareByDescending<Stats> { it.threePointersAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.PP3 -> careerStats.sortedWith(compareByDescending<Stats> { it.threePointersPercentage }.thenByDescending { it.winPercentage })
            CareerStatsSort.FTM -> careerStats.sortedWith(compareByDescending<Stats> { it.freeThrowsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.FTA -> careerStats.sortedWith(compareByDescending<Stats> { it.freeThrowsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.FTP -> careerStats.sortedWith(compareByDescending<Stats> { it.freeThrowsPercentage }.thenByDescending { it.winPercentage })
            CareerStatsSort.OREB -> careerStats.sortedWith(compareByDescending<Stats> { it.reboundsOffensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.DREB -> careerStats.sortedWith(compareByDescending<Stats> { it.reboundsDefensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.REB -> careerStats.sortedWith(compareByDescending<Stats> { it.reboundsTotal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.AST -> careerStats.sortedWith(compareByDescending<Stats> { it.assists.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.TOV -> careerStats.sortedWith(compareBy<Stats> { it.turnovers.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.STL -> careerStats.sortedWith(compareByDescending<Stats> { it.steals.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.BLK -> careerStats.sortedWith(compareByDescending<Stats> { it.blocks.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.PF -> careerStats.sortedWith(compareBy<Stats> { it.foulsPersonal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            CareerStatsSort.PLUSMINUS -> careerStats.sortedWith(compareByDescending<Stats> { it.plusMinus }.thenByDescending { it.winPercentage })
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    init {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshPlayerStats(playerId)
            }
            isRefreshingImp.value = false
        }
    }

    fun updateStatsSort(sort: CareerStatsSort) {
        statsSortImp.value = sort
    }
}