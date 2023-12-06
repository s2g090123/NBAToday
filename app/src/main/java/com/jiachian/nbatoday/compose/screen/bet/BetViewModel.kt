package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.bet.TurnTablePoints
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
private const val AnglePerRound = 360
private const val AnglePerHalfRound = AnglePerRound / 2
private const val OneSecondMs = 1000
private const val MinOneStep = 2
private const val MaxOneStep = 10
private const val MinDelay = 15
private const val MaxDelay = 25
private const val ReceivedDelay = 500L

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
    val betAndGame = repository.getBetsAndGames(account)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val askTurnTableVisibleImp = MutableStateFlow<TurnTablePoints?>(null)
    val askTurnTableVisible = askTurnTableVisibleImp.asStateFlow()

    private val tryTurnTableVisibleImp = MutableStateFlow<TurnTablePoints?>(null)
    val tryTurnTableVisible = tryTurnTableVisibleImp.asStateFlow()

    private val turnTableRunningImp = MutableStateFlow(false)
    val turnTableRunning = turnTableRunningImp.asStateFlow()

    private val turnTableAngleImp = MutableStateFlow(0f)
    val turnTableAngle = turnTableAngleImp.asStateFlow()

    private val rewardedPointsVisibleImp = MutableStateFlow<Long?>(null)
    val rewardedPointsVisible = rewardedPointsVisibleImp.asStateFlow()

    fun clickBetAndGame(betAndGame: BetAndGame) {
        when (betAndGame.game.gameStatus) {
            GameStatus.COMING_SOON -> {
                navigationController.navigateToTeam(betAndGame.game.homeTeamId)
            }
            GameStatus.PLAYING -> {
                navigationController.navigateToBoxScore(betAndGame.game.gameId)
            }
            GameStatus.FINAL -> {
                settleBet(betAndGame)
            }
        }
    }

    fun closeAskTurnTable() {
        askTurnTableVisibleImp.value = null
    }

    private fun settleBet(betAndGame: BetAndGame) {
        coroutineScope.launch(dispatcherProvider.io) {
            val (winPoint, losePoint) = repository.settleBet(betAndGame)
            askTurnTableVisibleImp.value = TurnTablePoints(
                winPoints = winPoint,
                losePoints = losePoint
            )
        }
    }

    fun showTurnTable(turnTablePoints: TurnTablePoints) {
        tryTurnTableVisibleImp.value = turnTablePoints
    }

    fun closeTurnTable() {
        tryTurnTableVisibleImp.value = null
        turnTableRunningImp.value = false
        turnTableAngleImp.value = 0f
    }

    fun startTurnTable(turnTablePoints: TurnTablePoints) {
        coroutineScope.launch {
            turnTableRunningImp.value = true
            withContext(dispatcherProvider.io) {
                val rewardedAngle = Random().nextInt(RandomBound).toFloat()
                val rewardedPoints = getRewardedPoints(turnTablePoints, rewardedAngle)
                repository.addPoints(rewardedPoints)
                var remainingTime = TurnTableDuration
                while (remainingTime > 0 || turnTableAngle.value != rewardedAngle) {
                    val currentAngle = turnTableAngle.value
                    val step = getTurnTableStep(remainingTime, currentAngle, rewardedAngle)
                    val delay = if (remainingTime <= 0) MaxDelay else MinDelay
                    delay(delay.toLong())
                    remainingTime -= delay
                    turnTableAngleImp.value = if (remainingTime <= 0 && currentAngle < rewardedAngle) {
                        (currentAngle + step).coerceAtMost(rewardedAngle)
                    } else {
                        (currentAngle + step) % AnglePerRound
                    }
                }
                delay(ReceivedDelay)
                closeTurnTable()
                rewardedPointsVisibleImp.value = rewardedPoints
            }
        }
    }

    private fun getTurnTableStep(
        remainingTime: Int,
        currentAngle: Float,
        rewardedAngle: Float,
    ): Int {
        return when {
            remainingTime <= 0 -> {
                val remainingAngle = AnglePerRound - currentAngle + rewardedAngle
                val rewardAngleDifference = rewardedAngle - currentAngle
                val isDifferenceTooLarge = currentAngle > rewardedAngle && remainingAngle > AnglePerHalfRound
                if (isDifferenceTooLarge || rewardAngleDifference > AnglePerHalfRound) {
                    2
                } else {
                    1
                }
            }
            else -> (remainingTime * 2 / OneSecondMs).coerceIn(MinOneStep, MaxOneStep)
        }
    }

    fun closeRewardedPoints() {
        rewardedPointsVisibleImp.value = null
    }

    private fun getRewardedPoints(
        turnTablePoints: TurnTablePoints,
        rewardedAngle: Float
    ): Long {
        return when (rewardedAngle) {
            in FirstSectorMinAngle..FirstSectorMaxAngle -> {
                -abs(turnTablePoints.winPoints) + abs(turnTablePoints.losePoints)
            }
            in SecondSectorMinAngle..SecondSectorMaxAngle -> {
                abs(turnTablePoints.winPoints) * MaxMagnification
            }
            in ThirdSectorMinAngle..ThirdSectorMaxAngle -> {
                -abs(turnTablePoints.winPoints)
            }
            else -> {
                abs(turnTablePoints.winPoints) + abs(turnTablePoints.losePoints)
            }
        }
    }
}
