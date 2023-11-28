package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.data.ScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.data.ScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreRowData
import com.jiachian.nbatoday.models.local.score.createRowData
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.utils.ScreenStateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    game: Game,
    private val repository: GameRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {

    private val gameId = game.gameId

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val boxScore = repository.getGameBoxScore(gameId)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    val date = boxScore.map {
        it?.gameDate ?: ""
    }.stateIn(coroutineScope, SharingStarted.Lazily, "")

    val isNotFound = boxScore.map {
        it == null
    }.stateIn(coroutineScope, SharingStarted.Lazily, true)

    val periodLabel = boxScore.map {
        it?.homeTeam?.periods?.map { period ->
            period.periodLabel
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    val statsLabels = LabelHelper.createScoreLabel()

    private val teamStatsLabels = LabelHelper.createScoreTeamLabel()

    private val leaderStatsLabels = LabelHelper.createScoreLeaderLabel()

    val homeTeam = boxScore.map { score ->
        score?.homeTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val awayTeam = boxScore.map { score ->
        score?.awayTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val homeLeader = boxScore.map { score ->
        val homePlayerId = game.homeLeaderPlayerId
        score?.homeTeam?.players?.firstOrNull {
            it.playerId == homePlayerId
        } ?: score?.homeTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val awayLeader = boxScore.map { score ->
        val awayPlayerId = game.awayLeadersPlayerId
        score?.awayTeam?.players?.firstOrNull {
            it.playerId == awayPlayerId
        } ?: score?.awayTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val selectedTabImp = MutableStateFlow(BoxScoreTab.HOME)
    val selectedTab = selectedTabImp.asStateFlow()
    val selectedTabIndex = selectedTab.map {
        BoxScoreTab.indexOf(it)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    val homeScoreRowData = boxScore.map { score ->
        score?.homeTeam?.players?.map { player ->
            val statsRowData = statsLabels.map { label ->
                label.transformRowData(player.statistics)
            }
            player.createRowData(statsRowData)
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val awayScoreRowData = boxScore.map { score ->
        score?.awayTeam?.players?.map { player ->
            val statsRowData = statsLabels.map { label ->
                label.transformRowData(player.statistics)
            }
            player.createRowData(statsRowData)
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val teamStatsRowData = boxScore.map { score ->
        teamStatsLabels.map { label ->
            label.transformRowData(score)
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val leaderStatsRowData = combine(
        homeLeader,
        awayLeader
    ) { home, away ->
        leaderStatsLabels.map { label ->
            label.transformRowData(home, away)
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    init {
        refreshScore()
    }

    fun refreshScore() {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.refreshGameBoxScore(gameId)
            }
            isRefreshingImp.value = false
        }
    }

    fun selectTab(tab: BoxScoreTab) {
        selectedTabImp.value = tab
    }

    private fun ScoreLabel.transformRowData(
        stats: BoxScore.BoxScoreTeam.Player.Statistics
    ): BoxScoreRowData.RowData {
        return BoxScoreRowData.RowData(
            value = LabelHelper.getValueByLabel(this, stats),
            textWidth = width,
            textAlign = textAlign
        )
    }

    private fun ScoreTeamLabel.transformRowData(score: BoxScore?): ScoreTeamRowData {
        return ScoreTeamRowData(
            homeValue = LabelHelper.getValueByLabel(this, score?.homeTeam?.statistics),
            awayValue = LabelHelper.getValueByLabel(this, score?.awayTeam?.statistics),
            label = this
        )
    }

    private fun ScoreLeaderLabel.transformRowData(
        homeLeader: BoxScore.BoxScoreTeam.Player?,
        awayLeader: BoxScore.BoxScoreTeam.Player?
    ): ScoreLeaderRowData {
        return ScoreLeaderRowData(
            homeValue = LabelHelper.getValueByLabel(this, homeLeader),
            awayValue = LabelHelper.getValueByLabel(this, awayLeader),
            label = this
        )
    }

    fun openPlayerCareer(playerId: Int) {
        screenStateHelper.openScreen(NbaScreenState.Player(playerId))
    }
}
