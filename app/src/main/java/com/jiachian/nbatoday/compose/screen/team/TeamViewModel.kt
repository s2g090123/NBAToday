package com.jiachian.nbatoday.compose.screen.team

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameStatusCardViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
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
    teamId: Int,
    private val teamRepository: TeamRepository,
    gameRepository: GameRepository,
    navigationController: NavigationController,
    private val composeViewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = Route.TEAM
) {
    private val team = NBATeam.getTeamById(teamId)
    val colors = team.colors

    val labels = LabelHelper.createTeamPlayerLabel()

    val gamesBefore = gameRepository.getGamesAndBetsBeforeByTeam(team.teamId, DateUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    val gamesAfter = gameRepository.getGamesAndBetsAfterByTeam(team.teamId, DateUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    private val teamAndPlayersStats = teamRepository.getTeamAndPlayers(team.teamId)

    val teamStats = teamAndPlayersStats.map {
        it?.team
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val teamRank = teamRepository.getTeamRank(team.teamId, team.conference)
        .stateIn(coroutineScope, SharingStarted.Eagerly, TeamRank.default())
    val teamStanding = teamRank.map {
        it.standing
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamPointsRank = teamRank.map {
        it.pointsRank
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamReboundsRank = teamRank.map {
        it.reboundsRank
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamAssistsRank = teamRank.map {
        it.assistsRank
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)
    val teamPlusMinusRank = teamRank.map {
        it.plusMinusRank
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    val isProgressing = teamRepository.isLoading
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
        val playerStats = stats?.teamPlayers ?: emptyList()
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
                val deferred1 = async { teamRepository.updateTeamStats() }
                val deferred2 = async { teamRepository.updateTeamPlayers(team.teamId) }
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
        navigationController.navigateToBoxScore(game.gameId)
    }

    fun openPlayerInfo(playerId: Int) {
        navigationController.navigateToPlayer(playerId)
    }

    fun createGameStatusCardViewModel(gameAndBets: GameAndBets): GameStatusCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBets = gameAndBets,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }
}
