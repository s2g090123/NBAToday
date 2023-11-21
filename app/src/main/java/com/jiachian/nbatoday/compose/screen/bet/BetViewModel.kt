package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.repository.bet.BetRepository
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
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
    private val repository: BetRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {

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
        when (betAndGame.game.gameStatus) {
            GameStatusCode.COMING_SOON -> screenStateHelper.openScreen(NbaScreenState.Team(betAndGame.game.homeTeam.team))
            GameStatusCode.PLAYING -> screenStateHelper.openScreen(NbaScreenState.BoxScore(betAndGame.game))
            GameStatusCode.FINAL -> settleBets(betAndGame)
        }
    }

    fun closeAskTurnTable() {
        askTurnTableImp.value = null
    }

    private fun settleBets(betAndGame: BetAndNbaGame) {
        coroutineScope.launch(dispatcherProvider.io) {
            val (winPoint, losePoint) = repository.settleBet(betAndGame)
            askTurnTableImp.value = BetsTurnTableData(
                winPoints = winPoint,
                losePoints = losePoint
            )
        }
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
