package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamRowData
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BoxScoreViewModel(
    private val gameId: String,
    private val repository: GameRepository,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = Route.BOX_SCORE
) {
    val isLoading = repository.isLoading

    init {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.updateBoxScore(gameId)
        }
    }

    private val boxScoreAndGame = repository.getBoxScoreAndGame(gameId)

    val boxScore = boxScoreAndGame.map {
        it?.boxScore
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val date = boxScore.map {
        it?.gameDate ?: ""
    }.stateIn(coroutineScope, SharingStarted.Lazily, "")

    val notFound = boxScore.map {
        it == null
    }.stateIn(coroutineScope, SharingStarted.Lazily, true)

    val periodLabels = boxScore.map {
        it?.homeTeam?.periods?.map { period ->
            period.periodLabel
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    val playerLabels = BoxScorePlayerLabel.values()
    private val teamLabels = BoxScoreTeamLabel.values()
    private val leaderLabels = BoxScoreLeaderLabel.values()

    private val selectedPlayerLabelImp = MutableStateFlow<BoxScorePlayerLabel?>(null)
    val selectedPlayerLabel = selectedPlayerLabelImp.asStateFlow()

    val homeTeam = boxScore.map { score ->
        score?.homeTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val awayTeam = boxScore.map { score ->
        score?.awayTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val homeLeader = boxScoreAndGame.map { boxScoreAndGame ->
        val score = boxScoreAndGame?.boxScore
        val leaderPlayerId = boxScoreAndGame?.game?.homeLeaderPlayerId
        score?.homeTeam?.players?.firstOrNull {
            it.playerId == leaderPlayerId
        } ?: score?.homeTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val awayLeader = boxScoreAndGame.map { boxScoreAndGame ->
        val score = boxScoreAndGame?.boxScore
        val leaderPlayerId = boxScoreAndGame?.game?.awayLeadersPlayerId
        score?.awayTeam?.players?.firstOrNull {
            it.playerId == leaderPlayerId
        } ?: score?.awayTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    val homePlayerRowData = boxScore.map { score ->
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
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val awayPlayerRowData = boxScore.map { score ->
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
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val teamRowData = boxScore.map { score ->
        teamLabels.map { label ->
            BoxScoreTeamRowData(
                label = label,
                home = LabelHelper.getValueByLabel(label, score?.homeTeam?.statistics),
                away = LabelHelper.getValueByLabel(label, score?.awayTeam?.statistics),
            )
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val leaderStatsRowData = combine(
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
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    fun selectPlayerLabel(label: BoxScorePlayerLabel?) {
        selectedPlayerLabelImp.value = label
    }

    fun openPlayerInfo(playerId: Int) {
        navigationController.navigateToPlayer(playerId)
    }
}
