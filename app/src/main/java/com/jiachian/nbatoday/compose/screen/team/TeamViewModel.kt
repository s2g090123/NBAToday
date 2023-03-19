package com.jiachian.nbatoday.compose.screen.team

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamViewModel(
    val teamId: Int,
    private val repository: BaseRepository,
    private val openScreen: (state: NbaState) -> Unit,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    val user = repository.user
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000L), null)

    private val team = DefaultTeam.getTeamById(teamId)

    val colors = DefaultTeam.getColorsById(teamId)

    private val games = repository.getGamesAndBets()
    val gamesBefore = games.map { games ->
        games.filter {
            (it.game.homeTeam.teamId == teamId || it.game.awayTeam.teamId == teamId) &&
                    it.game.gameDateTime.time <= NbaUtils.getCalendar().timeInMillis
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    val gamesAfter = games.map { games ->
        games.filter {
            (it.game.homeTeam.teamId == teamId || it.game.awayTeam.teamId == teamId) &&
                    it.game.gameDateTime.time > NbaUtils.getCalendar().timeInMillis
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    private val teamAndPlayersStats = repository.getTeamAndPlayersStats(teamId)

    val teamStats = teamAndPlayersStats.map {
        it?.teamStats
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    val teamRank = repository.getTeamRank(teamId, team.conference)
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)
    val teamPointsRank = repository.getTeamPointsRank(teamId)
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)
    val teamReboundsRank = repository.getTeamReboundsRank(teamId)
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)
    val teamAssistsRank = repository.getTeamAssistsRank(teamId)
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)
    val teamPlusMinusRank = repository.getTeamPlusMinusRank(teamId)
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()
    private val isTeamRefreshingImp = MutableStateFlow(false)
    val isTeamRefreshing = isTeamRefreshingImp.asStateFlow()
    private val selectPageIndexImp = MutableStateFlow(0)
    val selectPageIndex = selectPageIndexImp.asStateFlow()

    private val playerSortImp = MutableStateFlow(PlayerSort.PTS)
    val playerSort = playerSortImp.asStateFlow()
    val playerLabels = derivedStateOf {
        listOf(
            PlayerLabel(40.dp, "GP", TextAlign.End, PlayerSort.GP),
            PlayerLabel(40.dp, "W", TextAlign.End, PlayerSort.W),
            PlayerLabel(40.dp, "L", TextAlign.End, PlayerSort.L),
            PlayerLabel(64.dp, "WIN%", TextAlign.End, PlayerSort.WINP),
            PlayerLabel(64.dp, "PTS", TextAlign.End, PlayerSort.PTS),
            PlayerLabel(64.dp, "FGM", TextAlign.End, PlayerSort.FGM),
            PlayerLabel(64.dp, "FGA", TextAlign.End, PlayerSort.FGA),
            PlayerLabel(64.dp, "FG%", TextAlign.End, PlayerSort.FGP),
            PlayerLabel(64.dp, "3PM", TextAlign.End, PlayerSort.PM3),
            PlayerLabel(64.dp, "3PA", TextAlign.End, PlayerSort.PA3),
            PlayerLabel(64.dp, "3P%", TextAlign.End, PlayerSort.PP3),
            PlayerLabel(64.dp, "FTM", TextAlign.End, PlayerSort.FTM),
            PlayerLabel(64.dp, "FTA", TextAlign.End, PlayerSort.FTA),
            PlayerLabel(64.dp, "FT%", TextAlign.End, PlayerSort.FTP),
            PlayerLabel(48.dp, "OREB", TextAlign.End, PlayerSort.OREB),
            PlayerLabel(48.dp, "DREB", TextAlign.End, PlayerSort.DREB),
            PlayerLabel(48.dp, "REB", TextAlign.End, PlayerSort.REB),
            PlayerLabel(48.dp, "AST", TextAlign.End, PlayerSort.AST),
            PlayerLabel(48.dp, "TOV", TextAlign.End, PlayerSort.TOV),
            PlayerLabel(48.dp, "STL", TextAlign.End, PlayerSort.STL),
            PlayerLabel(48.dp, "BLK", TextAlign.End, PlayerSort.BLK),
            PlayerLabel(48.dp, "PF", TextAlign.End, PlayerSort.PF),
            PlayerLabel(48.dp, "+/-", TextAlign.End, PlayerSort.PLUSMINUS)
        )
    }

    val playersStats = combine(
        teamAndPlayersStats,
        playerSort
    ) { stats, sort ->
        val playerStats = stats?.playersStats ?: emptyList()
        when (sort) {
            PlayerSort.GP -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.W -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.win }.thenByDescending { it.winPercentage })
            PlayerSort.L -> playerStats.sortedWith(compareBy<PlayerStats> { it.lose }.thenByDescending { it.winPercentage })
            PlayerSort.WINP -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.winPercentage }.thenByDescending { it.winPercentage })
            PlayerSort.PTS -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.points.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.FGM -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.fieldGoalsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.FGA -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.fieldGoalsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.FGP -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.fieldGoalsPercentage }.thenByDescending { it.winPercentage })
            PlayerSort.PM3 -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.threePointersMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.PA3 -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.threePointersAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.PP3 -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.threePointersPercentage }.thenByDescending { it.winPercentage })
            PlayerSort.FTM -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.freeThrowsMade.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.FTA -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.freeThrowsAttempted.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.FTP -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.freeThrowsPercentage }.thenByDescending { it.winPercentage })
            PlayerSort.OREB -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.reboundsOffensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.DREB -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.reboundsDefensive.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.REB -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.reboundsTotal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.AST -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.assists.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.TOV -> playerStats.sortedWith(compareBy<PlayerStats> { it.turnovers.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.STL -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.steals.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.BLK -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.blocks.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.PF -> playerStats.sortedWith(compareBy<PlayerStats> { it.foulsPersonal.toDouble() / it.gamePlayed }.thenByDescending { it.winPercentage })
            PlayerSort.PLUSMINUS -> playerStats.sortedWith(compareByDescending<PlayerStats> { it.plusMinus }.thenByDescending { it.winPercentage })
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    init {
        updateStats()
    }

    fun updateStats() {
        coroutineScope.launch {
            isTeamRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                val deferred1 = async { repository.refreshTeamStats() }
                val deferred2 = async { repository.refreshTeamPlayersStats(teamId) }
                deferred1.await()
                deferred2.await()
            }
            isTeamRefreshingImp.value = false
        }
    }

    fun updatePageIndex(index: Int) {
        selectPageIndexImp.value = index.coerceIn(0, 2)
    }

    fun updatePlayerSort(label: PlayerLabel) {
        playerSortImp.value = label.sort
    }

    fun openGameBoxScore(game: NbaGame) {
        openScreen(
            NbaState.BoxScore(
                BoxScoreViewModel(
                    game = game,
                    repository = repository,
                    showPlayerCareer = this::openPlayerInfo,
                    coroutineScope = coroutineScope
                )
            )
        )
    }

    fun openPlayerInfo(playerId: Int) {
        openScreen(
            NbaState.Player(
                PlayerInfoViewModel(
                    playerId,
                    repository,
                    dispatcherProvider,
                    coroutineScope
                )
            )
        )
    }

    fun login(account: String, password: String) {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.login(account, password)
            }
            isRefreshingImp.value = false
        }
    }

    fun register(account: String, password: String) {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.register(account, password)
            }
            isRefreshingImp.value = false
        }
    }

    fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.bet(gameId, homePoints, awayPoints)
            }
            isRefreshingImp.value = false
        }
    }
}