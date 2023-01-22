package com.jiachian.nbatoday.compose.screen.home

import android.text.format.DateUtils
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

data class StandingLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign,
    val sort: StandingSort
)

class HomeViewModel(
    private val repository: BaseRepository,
    private val dataStore: NbaDataStore,
    private val openScreen: (state: NbaState) -> Unit
) : ComposeViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private val homeIndexImp = MutableStateFlow(0)
    val homeIndex = homeIndexImp.asStateFlow()

    // Schedule
    val scheduleDates: List<String> = getDateStrings()
    private val scheduleIndexImp = MutableStateFlow(scheduleDates.size / 2)
    val scheduleIndex = scheduleIndexImp.asStateFlow()
    private val scheduleGamesImp = NbaUtils.getCalendar().let {
        it.timeZone = TimeZone.getTimeZone("EST")
        it.set(Calendar.HOUR, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        repository.getGamesDuring(
            it.timeInMillis - DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE + 1),
            it.timeInMillis + DateUtils.DAY_IN_MILLIS * (SCHEDULE_DATE_RANGE)
        )
    }
    val scheduleGames = scheduleGamesImp.map {
        val calendar = NbaUtils.getCalendar()
        calendar.timeZone = TimeZone.getTimeZone("EST")
        it.groupBy { game ->
            calendar.time = game.gameDateTime
            String.format(
                "%d/%d/%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, mapOf())
    private val isRefreshingScheduleImp = MutableStateFlow(false)
    val isRefreshingSchedule = isRefreshingScheduleImp.asStateFlow()

    // Standing
    private val standingSortImp = MutableStateFlow(StandingSort.WINP)
    val standingSort = standingSortImp.asStateFlow()
    val teamStats = combine(
        repository.getTeamStats(),
        standingSort
    ) { teamStats, sort ->
        when (sort) {
            StandingSort.GP -> teamStats.sortedWith(compareByDescending<TeamStats> { it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.W -> teamStats.sortedWith(compareByDescending<TeamStats> { it.win }.thenByDescending { it.winPercentage })
            StandingSort.L -> teamStats.sortedWith(compareBy<TeamStats> { it.lose }.thenByDescending { it.winPercentage })
            StandingSort.WINP -> teamStats.sortedWith(compareByDescending<TeamStats> { it.winPercentage }.thenByDescending { it.winPercentage })
            StandingSort.PTS -> teamStats.sortedWith(compareByDescending<TeamStats> { it.points.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.FGM -> teamStats.sortedWith(compareByDescending<TeamStats> { it.fieldGoalsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.FGA -> teamStats.sortedWith(compareByDescending<TeamStats> { it.fieldGoalsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.FGP -> teamStats.sortedWith(compareByDescending<TeamStats> { it.fieldGoalsPercentage }.thenByDescending { it.winPercentage })
            StandingSort.PM3 -> teamStats.sortedWith(compareByDescending<TeamStats> { it.threePointersMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.PA3 -> teamStats.sortedWith(compareByDescending<TeamStats> { it.threePointersAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.PP3 -> teamStats.sortedWith(compareByDescending<TeamStats> { it.threePointersPercentage }.thenByDescending { it.winPercentage })
            StandingSort.FTM -> teamStats.sortedWith(compareByDescending<TeamStats> { it.freeThrowsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.FTA -> teamStats.sortedWith(compareByDescending<TeamStats> { it.freeThrowsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.FTP -> teamStats.sortedWith(compareByDescending<TeamStats> { it.freeThrowsPercentage }.thenByDescending { it.winPercentage })
            StandingSort.OREB -> teamStats.sortedWith(compareByDescending<TeamStats> { it.reboundsOffensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.DREB -> teamStats.sortedWith(compareByDescending<TeamStats> { it.reboundsDefensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.REB -> teamStats.sortedWith(compareByDescending<TeamStats> { it.reboundsTotal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.AST -> teamStats.sortedWith(compareByDescending<TeamStats> { it.assists.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.TOV -> teamStats.sortedWith(compareBy<TeamStats> { it.turnovers.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.STL -> teamStats.sortedWith(compareByDescending<TeamStats> { it.steals.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.BLK -> teamStats.sortedWith(compareByDescending<TeamStats> { it.blocks.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            StandingSort.PF -> teamStats.sortedWith(compareBy<TeamStats> { it.foulsPersonal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
        }.groupBy {
            it.teamConference
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, mapOf())
    private val isRefreshingTeamStatsImp = MutableStateFlow(false)
    val isRefreshingTeamStats = isRefreshingTeamStatsImp.asStateFlow()
    private val standingIndexImp = MutableStateFlow(0)
    val standingIndex = standingIndexImp.asStateFlow()
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

    init {
        updateTeamStats()
    }

    fun updateHomeIndex(index: Int) {
        homeIndexImp.value = index.coerceIn(0, 3)
    }

    fun updateScheduleIndex(index: Int) {
        scheduleIndexImp.value = index
    }

    private fun getDateStrings(): List<String> {
        val dateStrings = mutableListOf<String>()
        val calendar = NbaUtils.getCalendar()
        calendar.timeZone = TimeZone.getTimeZone("EST")
        val currentTime = calendar.time
        repeat(SCHEDULE_DATE_RANGE) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            dateStrings.add(
                0,
                String.format(
                    "%d/%d/%d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        calendar.time = currentTime
        dateStrings.add(
            String.format(
                "%d/%d/%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        repeat(SCHEDULE_DATE_RANGE) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            dateStrings.add(
                String.format(
                    "%d/%d/%d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        return dateStrings
    }

    fun updateTodaySchedule() {
        if (isRefreshingSchedule.value) return
        val dateString = scheduleDates.getOrNull(scheduleIndex.value) ?: return
        val dates = dateString.split("/")
        val year = dates.getOrNull(0)?.toInt() ?: return
        val month = dates.getOrNull(1)?.toInt() ?: return
        val day = dates.getOrNull(2)?.toInt() ?: return
        coroutineScope.launch {
            isRefreshingScheduleImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshSchedule(year, month, day)
            }
            isRefreshingScheduleImp.value = false
        }
    }

    fun openGameBoxScore(game: NbaGame) {
        openScreen(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = game,
                    repository = repository,
                    showPlayerCareer = { playerId ->
                        openScreen(
                            NbaState.Player(
                                PlayerInfoViewModel(playerId, repository, coroutineScope)
                            )
                        )
                    },
                    coroutineScope = coroutineScope
                )
            )
        )
    }

    fun updateTeamStats() {
        if (isRefreshingTeamStats.value) return
        coroutineScope.launch {
            isRefreshingTeamStatsImp.value = true
            withContext(Dispatchers.IO) {
                repository.refreshTeamStats()
            }
            isRefreshingTeamStatsImp.value = false
        }
    }

    fun updateStandingIndex(index: Int) {
        standingIndexImp.value = index.coerceIn(0, 1)
    }

    fun updateStandingSort(label: StandingLabel) {
        standingSortImp.value = label.sort
    }

    fun openTeamStats(teamId: Int) {
        openScreen(NbaState.Team(TeamViewModel(teamId, repository, openScreen, coroutineScope)))
    }

    fun updateTheme(teamId: Int, color: NBAColors) {
        updateColors(color)
        coroutineScope.launch(Dispatchers.IO) {
            dataStore.updateThemeColor(teamId)
        }
    }
}