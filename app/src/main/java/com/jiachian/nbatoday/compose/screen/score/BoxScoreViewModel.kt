package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    game: NbaGame,
    private val repository: BaseRepository,
    val showPlayerCareer: (playerId: Int) -> Unit,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel() {

    private val gameId = game.gameId

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val boxScore = repository.getGameBoxScore(gameId)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    val periodLabel = boxScore.map {
        it?.homeTeam?.periods?.map { period ->
            period.periodLabel
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    val homeLeader = boxScore.map { score ->
        val personId = if (game.gameStatus != GameStatusCode.COMING_SOON) {
            game.gameLeaders?.homeLeaders?.personId
        } else {
            game.teamLeaders?.homeLeaders?.personId
        } ?: game.pointsLeaders.firstOrNull { it.teamId == score?.homeTeam?.team?.teamId }?.personId
        score?.homeTeam?.players?.firstOrNull { player -> player.personId == personId }
            ?: score?.homeTeam?.players?.maxWithOrNull { p1, p2 ->
                (p1.statistics?.points ?: 0) - (p2.statistics?.points ?: 0)
            }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val awayLeader = boxScore.map { score ->
        val personId = if (game.gameStatus != GameStatusCode.COMING_SOON) {
            game.gameLeaders?.awayLeaders?.personId
        } else {
            game.teamLeaders?.awayLeaders?.personId
        } ?: game.pointsLeaders.firstOrNull { it.teamId == score?.awayTeam?.team?.teamId }?.personId
        score?.awayTeam?.players?.firstOrNull { player -> player.personId == personId }
            ?: score?.awayTeam?.players?.maxWithOrNull { p1, p2 ->
                (p1.statistics?.points ?: 0) - (p2.statistics?.points ?: 0)
            }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val selectPageImp = MutableStateFlow(BoxScoreTab.HOME)
    val selectPage = selectPageImp.asStateFlow()

    private val selectedLabelImp = MutableStateFlow<ScoreLabel?>(null)
    val selectedLabel = selectedLabelImp.asStateFlow()

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

    fun updateSelectPage(tab: BoxScoreTab) {
        selectPageImp.value = tab
    }

    fun selectLabel(label: ScoreLabel?) {
        selectedLabelImp.value = label
    }
}
