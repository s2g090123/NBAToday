package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreUI
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.utils.WhileSubscribed5000
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for handling business logic related to [BoxScoreScreen].
 *
 * @param gameId The ID of the game for which the box score is displayed.
 * @param repository The repository for interacting with [Game].
 * @property navigationController The controller for navigation within the app.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class BoxScoreViewModel(
    private val gameId: String,
    private val repository: GameRepository,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = MainRoute.BoxScore
) {
    // Update box score data into the repository
    init {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.insertBoxScore(gameId)
        }
    }

    // labels for box score player, team, and leader sections
    val playerLabels = BoxScorePlayerLabel.values()
    private val teamLabels = BoxScoreTeamLabel.values()
    private val leaderLabels = BoxScoreLeaderLabel.values()

    private val boxScoreAndGame = repository.getBoxScoreAndGame(gameId)
    private val boxScore = boxScoreAndGame.map {
        it?.boxScore
    }

    // date of the game (e.g. 2023-1-1)
    val date = boxScore.map {
        it?.gameDate ?: ""
    }.stateIn(coroutineScope, WhileSubscribed5000, "")

    private val periods = boxScore.map {
        it?.homeTeam?.periods?.map { period ->
            period.periodLabel
        }
    }.flowOn(dispatcherProvider.io)

    private val homeTeam = boxScore.map { score ->
        score?.homeTeam?.team ?: teamOfficial
    }
    private val awayTeam = boxScore.map { score ->
        score?.awayTeam?.team ?: teamOfficial
    }
    private val teamRowData = boxScore.map { score ->
        teamLabels.map { label ->
            BoxScoreTeamRowData(
                label = label,
                home = LabelHelper.getValueByLabel(label, score?.homeTeam?.statistics),
                away = LabelHelper.getValueByLabel(label, score?.awayTeam?.statistics),
            )
        }
    }.flowOn(dispatcherProvider.io)
    private val teamUI = combine(
        homeTeam,
        awayTeam,
        teamRowData
    ) { home, away, rowData ->
        BoxScoreUI.BoxScoreTeamsUI(
            home = home,
            away = away,
            rowData = rowData
        )
    }

    private val homeLeader = boxScoreAndGame.map { boxScoreAndGame ->
        val score = boxScoreAndGame?.boxScore
        val leaderPlayerId = boxScoreAndGame?.game?.homeLeaderId
        score?.homeTeam?.players?.firstOrNull {
            it.playerId == leaderPlayerId
        } ?: score?.homeTeam?.getMostPointsPlayer()
    }.flowOn(dispatcherProvider.io)
    private val awayLeader = boxScoreAndGame.map { boxScoreAndGame ->
        val score = boxScoreAndGame?.boxScore
        val leaderPlayerId = boxScoreAndGame?.game?.awayLeaderId
        score?.awayTeam?.players?.firstOrNull {
            it.playerId == leaderPlayerId
        } ?: score?.awayTeam?.getMostPointsPlayer()
    }.flowOn(dispatcherProvider.io)
    private val leaderRowData = combine(
        homeLeader,
        awayLeader
    ) { home, away ->
        leaderLabels.map { label ->
            BoxScoreLeaderRowData(
                label = label,
                home = LabelHelper.getValueByLabel(label, home),
                away = LabelHelper.getValueByLabel(label, away),
            )
        }
    }.flowOn(dispatcherProvider.io)
    private val leaderUI = combine(
        homeLeader,
        awayLeader,
        leaderRowData
    ) { home, away, rowData ->
        BoxScoreUI.BoxScoreLeadersUI(
            home = home,
            away = away,
            rowData = rowData
        )
    }

    private val homePlayerRowData = boxScore.map { score ->
        score?.homeTeam?.players?.map { player ->
            BoxScorePlayerRowData(
                player = player,
                data = playerLabels.map { label ->
                    BoxScorePlayerRowData.Data(
                        value = LabelHelper.getValueByLabel(label, player.statistics),
                        width = label.width,
                        align = label.align,
                    )
                }
            )
        } ?: emptyList()
    }.flowOn(dispatcherProvider.io)
    private val awayPlayerRowData = boxScore.map { score ->
        score?.awayTeam?.players?.map { player ->
            BoxScorePlayerRowData(
                player = player,
                data = playerLabels.map { label ->
                    BoxScorePlayerRowData.Data(
                        value = LabelHelper.getValueByLabel(label, player.statistics),
                        width = label.width,
                        align = label.align,
                    )
                }
            )
        } ?: emptyList()
    }.flowOn(dispatcherProvider.io)
    private val playerUI = combine(
        homePlayerRowData,
        awayPlayerRowData
    ) { home, away ->
        BoxScoreUI.BoxScorePlayersUI(
            home = home,
            away = away
        )
    }

    private val boxScoreUI = combine(
        boxScore,
        periods,
        playerUI,
        teamUI,
        leaderUI
    ) { boxScore, periods, player, team, leader ->
        if (boxScore == null || periods == null) return@combine null
        BoxScoreUI(
            boxScore = boxScore,
            periods = periods,
            players = player,
            teams = team,
            leaders = leader,
        )
    }
    val boxScoreUIState = combine(
        repository.loading,
        boxScoreUI
    ) { loading, boxScoreUI ->
        if (loading) return@combine UIState.Loading()
        UIState.Loaded(boxScoreUI)
    }.stateIn(coroutineScope, WhileSubscribed5000, UIState.Loading())

    private val selectedPlayerLabelImp = MutableStateFlow<BoxScorePlayerLabel?>(null)
    val selectedPlayerLabel = selectedPlayerLabelImp.asStateFlow()

    fun selectPlayerLabel(label: BoxScorePlayerLabel?) {
        selectedPlayerLabelImp.value = label
    }

    /**
     * Navigate to the player screen
     */
    fun openPlayerInfo(playerId: Int) {
        navigationController.navigateToPlayer(playerId)
    }
}
