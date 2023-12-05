package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.Route
import com.jiachian.nbatoday.repository.bet.BetRepository
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

private const val RandomBound = 359
private const val TurnTableDuration = 8000
private const val RoundAngle = 360
private const val RoundAngleHalf = RoundAngle / 2
private const val OneSecondMs = 1000
private const val MinOneStep = 2
private const val MaxOneStep = 10
private const val MinDelay = 15
private const val MaxDelay = 25
private const val StepDelay = 500L

private const val FirstSectorMinAngle = 0f
private const val FirstSectorMaxAngle = 89f
private const val SecondSectorMinAngle = 90f
private const val SecondSectorMaxAngle = 179f
private const val ThirdSectorMinAngle = 180f
private const val ThirdSectorMaxAngle = 269f

private const val MaxMagnification = 4

class BetViewModel(
    account: String,
    private val repository: BetRepository,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = Route.BET
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

    fun clickBetAndGame(betAndGame: BetAndGame) {
        when (betAndGame.game.gameStatus) {
            GameStatus.COMING_SOON -> {
                navigationController.navigateToTeam(betAndGame.game.homeTeamId)
            }
            GameStatus.PLAYING -> {
                navigationController.navigateToBoxScore(betAndGame.game.gameId)
            }
            GameStatus.FINAL -> {
                settleBets(betAndGame)
            }
        }
    }

    fun closeAskTurnTable() {
        askTurnTableImp.value = null
    }

    private fun settleBets(betAndGame: BetAndGame) {
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
            rewardAngleImp.value = Random().nextInt(RandomBound).toFloat()
            val rewardPoints = getRewardPoints(turnTableData, rewardAngle.value)
            withContext(dispatcherProvider.io) {
                repository.addPoints(rewardPoints)
            }
            isRefreshingImp.value = false
            isTurnTableStartingImp.value = true
            var remainTime = TurnTableDuration
            withContext(dispatcherProvider.io) {
                val current = currentAngle.value
                val reward = rewardAngle.value
                while (remainTime > 0 || current != reward) {
                    val step = when {
                        remainTime <= 0 -> {
                            val remainingAngle = RoundAngle - current + reward
                            val rewardAngleDifference = reward - current
                            val isDifferenceTooLarge = current > reward && remainingAngle > RoundAngleHalf
                            if (isDifferenceTooLarge || rewardAngleDifference > RoundAngleHalf) {
                                2
                            } else {
                                1
                            }
                        }
                        else -> (remainTime * 2 / OneSecondMs).coerceIn(MinOneStep, MaxOneStep)
                    }
                    val delay = if (remainTime <= 0) MaxDelay else MinDelay
                    delay(delay.toLong())
                    remainTime -= delay
                    currentAngleImp.value = if (remainTime <= 0 && current < reward) {
                        (current + step).coerceAtMost(reward)
                    } else {
                        (current + step) % RoundAngle
                    }
                }
                delay(StepDelay)
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
            in FirstSectorMinAngle..FirstSectorMaxAngle -> {
                -abs(turnTableData.winPoints) + abs(turnTableData.losePoints)
            }
            in SecondSectorMinAngle..SecondSectorMaxAngle -> {
                abs(turnTableData.winPoints) * MaxMagnification
            }
            in ThirdSectorMinAngle..ThirdSectorMaxAngle -> {
                -abs(turnTableData.winPoints)
            }
            else -> {
                abs(turnTableData.winPoints) + abs(turnTableData.losePoints)
            }
        }
    }
}
