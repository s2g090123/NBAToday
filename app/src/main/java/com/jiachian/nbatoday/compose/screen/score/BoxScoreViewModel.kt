package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ScoreLabel(
    val width: Dp,
    val text: String,
    val textAlign: TextAlign
)

class BoxScoreViewModel(
    game: NbaGame,
    private val repository: BaseRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
) : ComposeViewModel() {

    private val gameId = game.gameId

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val boxScore = repository.getGameBoxScore(gameId)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val homeLeader = boxScore.map {
        val personId = if (game.gameStatus != GameStatusCode.COMING_SOON) {
            game.gameLeaders?.homeLeaders?.personId
        } else {
            game.teamLeaders?.homeLeaders?.personId
        }
        it?.homeTeam?.players?.firstOrNull { player -> player.personId == personId }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
    val awayLeader = boxScore.map {
        val personId = if (game.gameStatus != GameStatusCode.COMING_SOON) {
            game.gameLeaders?.awayLeaders?.personId
        } else {
            game.teamLeaders?.awayLeaders?.personId
        }
        it?.awayTeam?.players?.firstOrNull { player -> player.personId == personId }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    private val selectIndexImp = MutableStateFlow(0)
    val selectIndex = selectIndexImp.asStateFlow()

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
            withContext(Dispatchers.IO) {
                repository.refreshGameBoxScore(gameId)
            }
            isRefreshingImp.value = false
        }
    }

    fun updateSelectIndex(index: Int) {
        selectIndexImp.value = index.coerceIn(0, 3)
    }
}