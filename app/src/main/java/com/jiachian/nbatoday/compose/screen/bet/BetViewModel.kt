package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTablePoints
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.bet.BetRepository
import java.util.Random
import kotlin.math.abs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

/**
 * ViewModel for handling business logic related to [BetScreen].
 *
 * @property account The user account associated with the logged-in user.
 * @property repository The repository for interacting with [BetAndGame].
 * @property navigationController The controller for navigation within the app.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with unconfined dispatcher).
 */
class BetViewModel(
    account: String,
    private val repository: BetRepository,
    navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(
    coroutineScope = coroutineScope,
    navigationController = navigationController,
    route = MainRoute.Bet
) {
    // state representing the stats of [BetAndGame]
    val betsAndGamesState = repository
        .getBetsAndGames(account)
        .map { UIState.Loaded(it) }
        .stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    // the current points on the turn table
    private val turnTablePointsImp = MutableStateFlow<TurnTablePoints?>(null)
    val turnTablePoints = turnTablePointsImp.asStateFlow()

    // whether the turn table is currently running
    private val turnTableRunningImp = MutableStateFlow(false)
    val turnTableRunning = turnTableRunningImp.asStateFlow()

    // the current angle of the turn table
    private val turnTableAngleImp = MutableStateFlow(0f)
    val turnTableAngle = turnTableAngleImp.asStateFlow()

    // the visibility of the turn table
    private val turnTableVisibleImp = MutableStateFlow(false)
    val turnTableVisible = turnTableVisibleImp.asStateFlow()

    // the points rewarded after the turn table animation
    private val rewardedPointsImp = MutableStateFlow<Long?>(null)
    val rewardedPoints = rewardedPointsImp.asStateFlow()

    /**
     * Handles the click event on a specific [BetAndGame].
     *
     * @param betAndGame The clicked [BetAndGame].
     */
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

    private fun settleBet(betAndGame: BetAndGame) {
        coroutineScope.launch(dispatcherProvider.io) {
            val (win, lose) = repository.settleBet(betAndGame)
            turnTablePointsImp.value = TurnTablePoints(
                win = win,
                lose = lose
            )
        }
    }

    fun closeTurnTable() {
        turnTableVisibleImp.value = false
        turnTablePointsImp.value = null
        turnTableRunningImp.value = false
        turnTableAngleImp.value = 0f
    }

    fun showTurnTable() {
        turnTableVisibleImp.value = true
    }

    /**
     * Starts the turn table animation with the specified points configuration.
     *
     * @param turnTablePoints The points configuration for the turn table.
     */
    fun startTurnTable(turnTablePoints: TurnTablePoints) {
        coroutineScope.launch(dispatcherProvider.io) {
            turnTableRunningImp.value = true
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
            rewardedPointsImp.value = rewardedPoints
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
                val differenceTooLarge = currentAngle > rewardedAngle && remainingAngle > AnglePerHalfRound
                if (differenceTooLarge || rewardAngleDifference > AnglePerHalfRound) {
                    2
                } else {
                    1
                }
            }
            else -> (remainingTime * 2 / OneSecondMs).coerceIn(MinOneStep, MaxOneStep)
        }
    }

    fun closeRewardedPoints() {
        rewardedPointsImp.value = null
    }

    private fun getRewardedPoints(
        turnTablePoints: TurnTablePoints,
        rewardedAngle: Float
    ): Long {
        return when (rewardedAngle) {
            in FirstSectorMinAngle..FirstSectorMaxAngle -> {
                -abs(turnTablePoints.win) + abs(turnTablePoints.lose)
            }
            in SecondSectorMinAngle..SecondSectorMaxAngle -> {
                abs(turnTablePoints.win) * MaxMagnification
            }
            in ThirdSectorMinAngle..ThirdSectorMaxAngle -> {
                -abs(turnTablePoints.win)
            }
            else -> {
                abs(turnTablePoints.win) + abs(turnTablePoints.lose)
            }
        }
    }
}
