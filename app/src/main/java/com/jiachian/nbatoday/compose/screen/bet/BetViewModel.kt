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
import java.util.Random
import kotlin.math.abs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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

    private val isTurnTableStartingImp = MutableStateFlow(false)
    val isTurnTableStarting = isTurnTableStartingImp.asStateFlow()

    private val rewardAngleImp = MutableStateFlow(0f)
    val rewardAngle = rewardAngleImp.asStateFlow()

    private val currentAngleImp = MutableStateFlow(0f)
    val currentAngle = currentAngleImp.asStateFlow()

    private val showRewardPointsImp = MutableStateFlow<Long?>(null)
    val showRewardPoints = showRewardPointsImp.asStateFlow()

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

    private fun settleBets(betAndGame: BetAndNbaGame) {
        val winPoint = (
            if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
                betAndGame.bets.homePoints
            } else {
                betAndGame.bets.awayPoints
            }
            ) * 2
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

    fun showTurnTable(turnTableData: BetsTurnTableData) {
        showTryTurnTableImp.value = turnTableData
    }

    fun closeTurnTable() {
        showTryTurnTableImp.value = null
        isTurnTableStartingImp.value = false
        currentAngleImp.value = 0f
    }

    fun startTurnTable(turnTableData: BetsTurnTableData) {
        coroutineScope.launch {
            isRefreshingImp.value = true
            rewardAngleImp.value = Random().nextInt(359).toFloat()
            val rewardPoints = getRewardPoints(turnTableData, rewardAngle.value)
            withContext(dispatcherProvider.io) {
                repository.addPoints(rewardPoints)
            }
            isRefreshingImp.value = false
            isTurnTableStartingImp.value = true
            var remainTime = 8000
            withContext(dispatcherProvider.io) {
                while (remainTime > 0 || currentAngle.value != rewardAngle.value) {
                    val step = when {
                        remainTime <= 0 -> {
                            if (
                                (currentAngle.value > rewardAngle.value && 360 - currentAngle.value + rewardAngle.value > 180) ||
                                rewardAngle.value - currentAngle.value > 180
                            ) {
                                2
                            } else {
                                1
                            }
                        }
                        else -> (remainTime * 2 / 1000).coerceIn(2, 10)
                    }
                    val delay = if (remainTime <= 0) 25 else 15
                    delay(delay.toLong())
                    remainTime -= delay
                    currentAngleImp.value =
                        if (remainTime <= 0 && currentAngle.value < rewardAngle.value) {
                            (currentAngle.value + step).coerceAtMost(rewardAngle.value)
                        } else {
                            (currentAngle.value + step) % 360
                        }
                }
                delay(500)
                closeTurnTable()
                showRewardPointsImp.value = rewardPoints
            }
        }
    }

    fun closeRewardPointsDialog() {
        showRewardPointsImp.value = null
    }

    private fun getRewardPoints(
        turnTableData: BetsTurnTableData,
        angle: Float
    ): Long {
        return when (angle) {
            in 0f..89f -> {
                -abs(turnTableData.winPoints) + abs(turnTableData.losePoints)
            }
            in 90f..179f -> {
                abs(turnTableData.winPoints) * 4
            }
            in 180f..269f -> {
                -abs(turnTableData.winPoints)
            }
            else -> {
                abs(turnTableData.winPoints) + abs(turnTableData.losePoints)
            }
        }
    }
}
