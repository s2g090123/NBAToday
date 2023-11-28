package com.jiachian.nbatoday.compose.screen.team

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.ScreenStateHelper
import com.jiachian.nbatoday.utils.decimalFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamViewModel(
    val team: NBATeam,
    private val teamRepository: TeamRepository,
    gameRepository: GameRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {
    val colors = team.colors

    val labels = LabelHelper.createTeamPlayerLabel()

    val gamesBefore = gameRepository.getGamesAndBetsBeforeByTeam(team.teamId, NbaUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    val gamesAfter = gameRepository.getGamesAndBetsAfterByTeam(team.teamId, NbaUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    private val teamAndPlayersStats = teamRepository.getTeamAndPlayersStats(team.teamId)

    val teamStats = teamAndPlayersStats.map {
        it?.team
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    val teamRank = teamRepository.getTeamRank(team.teamId, team.conference)
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamPointsRank = teamRepository.getTeamPointsRank(team.teamId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamReboundsRank = teamRepository.getTeamReboundsRank(team.teamId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamAssistsRank = teamRepository.getTeamAssistsRank(team.teamId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamPlusMinusRank = teamRepository.getTeamPlusMinusRank(team.teamId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    val isProgressing = teamRepository.isProgressing
    private val isTeamRefreshingImp = MutableStateFlow(false)
    val isTeamRefreshing = isTeamRefreshingImp.asStateFlow()
    private val selectPageImp = MutableStateFlow(TeamPageTab.PLAYERS)
    val selectPage = selectPageImp.asStateFlow()

    private val playerSortImp = MutableStateFlow(PlayerSort.PTS)
    val playerSort = playerSortImp.asStateFlow()

    val playersStats = combine(
        teamAndPlayersStats,
        playerSort
    ) { stats, sort ->
        val playerStats = stats?.playersStats ?: emptyList()
        when (sort) {
            PlayerSort.GP -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.W -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.win
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.L -> playerStats.sortedWith(
                compareBy<TeamPlayer> {
                    it.lose
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.WINP -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.winPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PTS -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.points.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FGM -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.fieldGoalsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FGA -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.fieldGoalsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FGP -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.fieldGoalsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PM3 -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.threePointersMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PA3 -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.threePointersAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PP3 -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.threePointersPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FTM -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.freeThrowsMade.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FTA -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.freeThrowsAttempted.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.FTP -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.freeThrowsPercentage
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.OREB -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.reboundsOffensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.DREB -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.reboundsDefensive.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.REB -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.reboundsTotal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.AST -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.assists.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.TOV -> playerStats.sortedWith(
                compareBy<TeamPlayer> {
                    it.turnovers.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.STL -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.steals.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.BLK -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.blocks.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PF -> playerStats.sortedWith(
                compareBy<TeamPlayer> {
                    it.foulsPersonal.toDouble() / it.gamePlayed
                }.thenByDescending {
                    it.winPercentage
                }
            )

            PlayerSort.PLUSMINUS -> playerStats.sortedWith(
                compareByDescending<TeamPlayer> {
                    it.plusMinus
                }.thenByDescending {
                    it.winPercentage
                }
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    val isDataLoaded = teamStats.combine(playersStats) { a, b ->
        a != null && b.isNotEmpty()
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)

    val getStatsTextByLabel = { label: TeamPlayerLabel, stats: TeamPlayer ->
        when (label) {
            TeamPlayerLabel.GP -> stats.gamePlayed
            TeamPlayerLabel.WIN -> stats.win
            TeamPlayerLabel.LOSE -> stats.lose
            TeamPlayerLabel.WINP -> stats.winPercentage.decimalFormat()
            TeamPlayerLabel.PTS -> stats.pointsAverage.decimalFormat()
            TeamPlayerLabel.FGM -> stats.fieldGoalsMadeAverage.decimalFormat()
            TeamPlayerLabel.FGA -> stats.fieldGoalsAttemptedAverage.decimalFormat()
            TeamPlayerLabel.FGP -> stats.fieldGoalsPercentage.decimalFormat()
            TeamPlayerLabel.PM3 -> stats.threePointersMadeAverage.decimalFormat()
            TeamPlayerLabel.PA3 -> stats.threePointersAttemptedAverage.decimalFormat()
            TeamPlayerLabel.PP3 -> stats.threePointersPercentage.decimalFormat()
            TeamPlayerLabel.FTM -> stats.freeThrowsMadeAverage.decimalFormat()
            TeamPlayerLabel.FTA -> stats.freeThrowsAttemptedAverage.decimalFormat()
            TeamPlayerLabel.FTP -> stats.freeThrowsPercentage.decimalFormat()
            TeamPlayerLabel.OREB -> stats.reboundsOffensiveAverage.decimalFormat()
            TeamPlayerLabel.DREB -> stats.reboundsDefensiveAverage.decimalFormat()
            TeamPlayerLabel.REB -> stats.reboundsTotalAverage.decimalFormat()
            TeamPlayerLabel.AST -> stats.assistsAverage.decimalFormat()
            TeamPlayerLabel.TOV -> stats.turnoversAverage.decimalFormat()
            TeamPlayerLabel.STL -> stats.stealsAverage.decimalFormat()
            TeamPlayerLabel.BLK -> stats.blocksAverage.decimalFormat()
            TeamPlayerLabel.PF -> stats.foulsPersonalAverage.decimalFormat()
            TeamPlayerLabel.PLUSMINUS -> stats.plusMinus
        }.toString()
    }

    init {
        updateStats()
    }

    fun updateStats() {
        coroutineScope.launch {
            isTeamRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                val deferred1 = async { teamRepository.refreshTeamStats(null) }
                val deferred2 = async { teamRepository.refreshTeamPlayersStats(team.teamId) }
                deferred1.await()
                deferred2.await()
            }
            isTeamRefreshingImp.value = false
        }
    }

    fun updateSelectPage(page: TeamPageTab) {
        selectPageImp.value = page
    }

    fun updatePlayerSort(sort: PlayerSort) {
        playerSortImp.value = sort
    }

    fun openGameBoxScore(game: Game) {
        screenStateHelper.openScreen(NbaScreenState.BoxScore(game))
    }

    fun openPlayerInfo(playerId: Int) {
        screenStateHelper.openScreen(NbaScreenState.Player(playerId))
    }

    fun createGameStatusCardViewModel(gameAndBet: NbaGameAndBet): GameStatusCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBet = gameAndBet,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }
}
