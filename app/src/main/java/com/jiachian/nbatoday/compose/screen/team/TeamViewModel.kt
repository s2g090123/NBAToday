package com.jiachian.nbatoday.compose.screen.team

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.compose.screen.team.models.TeamUI
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for handling business logic related to [TeamScreen].
 *
 * @param teamId The ID of the team for which details are displayed.
 * @param teamRepository The repository for interacting with [Team].
 * @param gameRepository The repository for interacting with [Game].
 * @property navigationController The controller for navigation within the app.
 * @property composeViewModelProvider The provider for creating ComposeViewModel instances.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with unconfined dispatcher).
 */
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
    route = MainRoute.Team
) {
    private val team = NBATeam.getTeamById(teamId)
    val colors = team.colors


    // Asynchronous initialization to update teams and team players.
    init {
        coroutineScope.launch(dispatcherProvider.io) {
            val deferred1 = async { teamRepository.insertTeams() }
            val deferred2 = async { teamRepository.updateTeamPlayers(team.teamId) }
            awaitAll(deferred1, deferred2)
        }
    }

    val labels = TeamPlayerLabel.values()

    /**
     * the games before/after today.
     */
    val gamesBefore = gameRepository.getGamesAndBetsBefore(
        team.teamId,
        DateUtils.getCalendar().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    ).map {
        UIState.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())
    val gamesAfter = gameRepository.getGamesAndBetsAfter(
        team.teamId,
        DateUtils.getCalendar().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    ).map {
        UIState.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val teamAndPlayers = teamRepository.getTeamAndPlayers(team.teamId)

    private val teamStats = teamAndPlayers.map {
        it?.team
    }

    private val teamRank = teamRepository.getTeamRank(team.teamId, team.conference)

    private val playerSortingImp = MutableStateFlow(TeamPlayerSorting.PTS)
    val playerSorting = playerSortingImp.asStateFlow()

    private val playerRowData = teamAndPlayers.map { teamAndPlayers ->
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
    }.flowOn(dispatcherProvider.io)

    private val sortedPlayerRowData = combine(
        playerRowData,
        playerSorting
    ) { rowData, sorting ->
        rowData.sortedWith(sorting)
    }.flowOn(dispatcherProvider.io)

    private val teamUI = combine(
        teamStats,
        teamRank,
        sortedPlayerRowData
    ) { team, rank, players ->
        team ?: return@combine null
        TeamUI(
            team = team,
            rank = rank,
            players = players,
        )
    }
    val teamUIState = combine(
        teamRepository.loading,
        teamUI
    ) { loading, teamUI ->
        if (loading) return@combine UIState.Loading()
        UIState.Loaded(teamUI)
    }.stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val gameCardViewModelMap = mutableMapOf<GameAndBets, GameCardViewModel>()

    /**
     * Update the player sorting based on the provided [sorting] criteria.
     *
     * @param sorting The sorting criteria for team players.
     */
    fun updatePlayerSorting(sorting: TeamPlayerSorting) {
        playerSortingImp.value = sorting
    }

    /**
     * Handle the click event on a game card and navigates to the box score screen.
     *
     * @param game The clicked game.
     */
    fun onGameCardClick(game: Game) {
        navigationController.navigateToBoxScore(game.gameId)
    }

    /**
     * Handle the click event on a player and navigates to the player screen.
     *
     * @param playerId The ID of the clicked player.
     */
    fun onPlayerClick(playerId: Int) {
        navigationController.navigateToPlayer(playerId)
    }

    fun getGameCardViewModel(gameAndBets: GameAndBets): GameCardViewModel {
        return gameCardViewModelMap.getOrPut(gameAndBets) {
            composeViewModelProvider.getGameCardViewModel(
                gameAndBets = gameAndBets,
                dispatcherProvider = dispatcherProvider,
                coroutineScope = coroutineScope
            )
        }
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
