package com.jiachian.nbatoday.compose.screen.team

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
    init {
        coroutineScope.launch(dispatcherProvider.io) {
            val deferred1 = async { teamRepository.updateTeamStats() }
            val deferred2 = async { teamRepository.updateTeamPlayers(team.teamId) }
            deferred1.await()
            deferred2.await()
        }
    }

    private val team = NBATeam.getTeamById(teamId)
    val colors = team.colors

    val labels = LabelHelper.createTeamPlayerLabel()

    val gamesBefore = gameRepository.getGamesAndBetsBeforeByTeam(team.teamId, DateUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())
    val gamesAfter = gameRepository.getGamesAndBetsAfterByTeam(team.teamId, DateUtils.getCalendar().timeInMillis)
        .stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())
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

    private val playerSortingImp = MutableStateFlow(TeamPlayerSorting.PTS)
    val playerSorting = playerSortingImp.asStateFlow()

    private val playerRowData = teamAndPlayersStats.map { teamAndPlayers ->
        teamAndPlayers?.teamPlayers?.map { player ->
            TeamPlayerRowData(
                player = player,
                data = labels.map { label ->
                    TeamPlayerRowData.Data(
                        value = LabelHelper.getValueByLabel(label, player),
                        width = label.width,
                        sorting = label.sorting,
                    )
                }
            )
        } ?: emptyList()
    }

    val sortedPlayerRowData = combine(
        playerRowData,
        playerSorting
    ) { rowData, sorting ->
        rowData.sortedWith(sorting)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val isLoading = combine(
        teamRepository.isLoading,
        teamStats,
    ) { loading, team ->
        loading || team == null
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)

    fun updatePlayerSorting(sorting: TeamPlayerSorting) {
        playerSortingImp.value = sorting
    }

    fun openGameBoxScore(game: Game) {
        navigationController.navigateToBoxScore(game.gameId)
    }

    fun openPlayerInfo(playerId: Int) {
        navigationController.navigateToPlayer(playerId)
    }

    fun createGameStatusCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return composeViewModelProvider.getGameStatusCardViewModel(
            gameAndBets = gameAndBets,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    private fun List<TeamPlayerRowData>.sortedWith(sorting: TeamPlayerSorting): List<TeamPlayerRowData> {
        val comparator = when (sorting) {
            TeamPlayerSorting.GP -> compareByDescending { it.player.gamePlayed }
            TeamPlayerSorting.W -> compareByDescending { it.player.win }
            TeamPlayerSorting.L -> compareBy { it.player.lose }
            TeamPlayerSorting.WINP -> compareByDescending { it.player.winPercentage }
            TeamPlayerSorting.PTS -> compareByDescending { it.player.pointsAverage }
            TeamPlayerSorting.FGM -> compareByDescending { it.player.fieldGoalsMadeAverage }
            TeamPlayerSorting.FGA -> compareByDescending { it.player.fieldGoalsAttemptedAverage }
            TeamPlayerSorting.FGP -> compareByDescending { it.player.fieldGoalsPercentage }
            TeamPlayerSorting.PM3 -> compareByDescending { it.player.threePointersMadeAverage }
            TeamPlayerSorting.PA3 -> compareByDescending { it.player.threePointersAttemptedAverage }
            TeamPlayerSorting.PP3 -> compareByDescending { it.player.threePointersPercentage }
            TeamPlayerSorting.FTM -> compareByDescending { it.player.freeThrowsMadeAverage }
            TeamPlayerSorting.FTA -> compareByDescending { it.player.freeThrowsAttemptedAverage }
            TeamPlayerSorting.FTP -> compareByDescending { it.player.freeThrowsPercentage }
            TeamPlayerSorting.OREB -> compareByDescending { it.player.reboundsOffensiveAverage }
            TeamPlayerSorting.DREB -> compareByDescending { it.player.reboundsDefensiveAverage }
            TeamPlayerSorting.REB -> compareByDescending { it.player.reboundsTotalAverage }
            TeamPlayerSorting.AST -> compareByDescending { it.player.assistsAverage }
            TeamPlayerSorting.TOV -> compareBy { it.player.turnoversAverage }
            TeamPlayerSorting.STL -> compareByDescending { it.player.stealsAverage }
            TeamPlayerSorting.BLK -> compareByDescending { it.player.blocksAverage }
            TeamPlayerSorting.PF -> compareBy { it.player.foulsPersonalAverage }
            TeamPlayerSorting.PLUSMINUS -> compareByDescending<TeamPlayerRowData> { it.player.plusMinus }
        }.thenByDescending { it.player.winPercentage }
        return sortedWith(comparator)
    }
}
