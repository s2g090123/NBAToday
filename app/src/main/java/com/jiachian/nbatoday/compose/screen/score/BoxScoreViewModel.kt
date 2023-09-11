package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    val homeLeader = boxScore.map { score ->
        val personId = if (game.gameStatus != GameStatusCode.COMING_SOON) {
            game.gameLeaders?.homeLeaders?.personId
        } else {
            game.teamLeaders?.homeLeaders?.personId
        } ?: game.pointsLeaders.firstOrNull { it.teamId == score?.homeTeam?.teamId }?.personId
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
        } ?: game.pointsLeaders.firstOrNull { it.teamId == score?.awayTeam?.teamId }?.personId
        score?.awayTeam?.players?.firstOrNull { player -> player.personId == personId }
            ?: score?.awayTeam?.players?.maxWithOrNull { p1, p2 ->
                (p1.statistics?.points ?: 0) - (p2.statistics?.points ?: 0)
            }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val selectPageImp = MutableStateFlow(BoxScoreTab.HOME)
    val selectPage = selectPageImp.asStateFlow()

    val scoreLabel = derivedStateOf {
        listOf(
            ScoreLabel(72.dp, "MIN", TextAlign.Center),
            ScoreLabel(72.dp, "FGM-A", TextAlign.End),
            ScoreLabel(72.dp, "3PM-A", TextAlign.End),
            ScoreLabel(72.dp, "FTM-A", TextAlign.End),
            ScoreLabel(40.dp, "+/-", TextAlign.End),
            ScoreLabel(40.dp, "OR", TextAlign.End),
            ScoreLabel(40.dp, "DR", TextAlign.End),
            ScoreLabel(40.dp, "TR", TextAlign.End),
            ScoreLabel(40.dp, "AS", TextAlign.End),
            ScoreLabel(40.dp, "PF", TextAlign.End),
            ScoreLabel(40.dp, "ST", TextAlign.End),
            ScoreLabel(40.dp, "TO", TextAlign.End),
            ScoreLabel(40.dp, "BS", TextAlign.End),
            ScoreLabel(40.dp, "BA", TextAlign.End),
            ScoreLabel(48.dp, "PTS", TextAlign.End),
            ScoreLabel(48.dp, "EFF", TextAlign.End)
        )
    }

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
}
