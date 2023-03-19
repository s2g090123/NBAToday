package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.player.PlayerInfoViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BetViewModel(
    account: String,
    private val repository: BaseRepository,
    private val openScreen: (state: NbaState) -> Unit,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) {

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val betAndGame = repository.getBetsAndGames(account)
        .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    private val askTurnTableImp = MutableStateFlow<BetsTurnTableData?>(null)
    val askTurnTable = askTurnTableImp.asStateFlow()

    private val showTryTurnTableImp = MutableStateFlow<BetsTurnTableData?>(null)
    val showTryTurnTable = showTryTurnTableImp.asStateFlow()

    fun clickBetAndGame(betAndGame: BetAndNbaGame) {
        if (betAndGame.game.gameStatus == GameStatusCode.COMING_SOON) {
            openScreen(
                NbaState.Team(
                    TeamViewModel(betAndGame.game.homeTeam.teamId, repository, openScreen)
                )
            )
        } else if (betAndGame.game.gameStatus != GameStatusCode.FINAL) {
            openScreen(
                NbaState.BoxScore(
                    BoxScoreViewModel(
                        betAndGame.game,
                        repository,
                        showPlayerCareer = {
                            openScreen(
                                NbaState.Player(
                                    PlayerInfoViewModel(it, repository)
                                )
                            )
                        }
                    )
                )
            )
        } else {
            settleBets(betAndGame)
        }
    }

    fun closeAskTurnTable() {
        askTurnTableImp.value = null
    }

    fun closeTurnTable() {
        showTryTurnTableImp.value = null
    }

    private fun settleBets(betAndGame: BetAndNbaGame) {
        val winPoint = (if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
            betAndGame.bets.homePoints
        } else {
            betAndGame.bets.awayPoints
        }) * 2
        val losePoint = if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
            betAndGame.bets.awayPoints
        } else {
            betAndGame.bets.homePoints
        }
        coroutineScope.launch(dispatcherProvider.io) {
            repository.addPoints(winPoint)
            repository.deleteBets(betAndGame.bets)
        }
        askTurnTableImp.value = BetsTurnTableData(
            winPoints = winPoint,
            losePoints = losePoint
        )
    }

    fun startTurnTable(turnTableData: BetsTurnTableData) {
        showTryTurnTableImp.value = turnTableData
    }

    fun addPoints(points: Long) {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.addPoints(points)
            }
            isRefreshingImp.value = false
        }
    }
}