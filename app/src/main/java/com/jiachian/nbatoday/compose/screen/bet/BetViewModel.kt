package com.jiachian.nbatoday.compose.screen.bet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.utils.WhileSubscribed5000
import java.util.Random
import kotlin.math.abs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.mapLatest
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
 * @property repository The repository for interacting with [BetAndGame].
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BetViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BetRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val account: String = savedStateHandle[MainRoute.Bet.param] ?: throw Exception("account is null.")

    // state representing the stats of [BetAndGame]
    val betsAndGamesState = repository
        .getBetsAndGames(account)
        .mapLatest { UIState.Loaded(it) }
        .stateIn(viewModelScope, WhileSubscribed5000, UIState.Loading())

    private var turnTableUIStateImp = mutableStateOf<TurnTableUIState>(TurnTableUIState.Idle)
    val turnTableUIState by turnTableUIStateImp

    fun settleBet(betAndGame: BetAndGame) {
        viewModelScope.launch(dispatcherProvider.io) {
            val (win, lose) = repository.settleBet(betAndGame)
            turnTableUIStateImp.value = TurnTableUIState.Asking(Win(win), Lose(lose))
        }
    }

    fun closeTurnTable() {
        turnTableUIStateImp.value = TurnTableUIState.Idle
    }

    fun showTurnTable(win: Win, lose: Lose) {
        turnTableUIStateImp.value = TurnTableUIState.TurnTable(win, lose)
    }

    /**
     * Starts the turn table animation with the specified points configuration.
     *
     * @param win The win points configuration for the turn table.
     * @param lose The lose points configuration for the turn table.
     */
    fun startTurnTable(win: Win, lose: Lose) {
        val state = turnTableUIState
        if (state !is TurnTableUIState.TurnTable) {
            turnTableUIStateImp.value = TurnTableUIState.Idle
            return
        }
        viewModelScope.launch(dispatcherProvider.io) {
            state.running = true
            val rewardedAngle = Random().nextInt(RandomBound).toFloat()
            val rewardedPoints = getRewardedPoints(win, lose, rewardedAngle)
            repository.addPoints(rewardedPoints)
            var remainingTime = TurnTableDuration
            while (remainingTime > 0 || state.angle != rewardedAngle) {
                val currentAngle = state.angle
                val step = getTurnTableStep(remainingTime, currentAngle, rewardedAngle)
                val delay = if (remainingTime <= 0) MaxDelay else MinDelay
                delay(delay.toLong())
                remainingTime -= delay
                state.angle = if (remainingTime <= 0 && currentAngle < rewardedAngle) {
                    (currentAngle + step).coerceAtMost(rewardedAngle)
                } else {
                    (currentAngle + step) % AnglePerRound
                }
            }
            delay(ReceivedDelay)
            turnTableUIStateImp.value = TurnTableUIState.Rewarded(rewardedPoints)
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
                if (differenceTooLarge || rewardAngleDifference > AnglePerHalfRound) 2 else 1
            }
            else -> (remainingTime * 2 / OneSecondMs).coerceIn(MinOneStep, MaxOneStep)
        }
    }

    private fun getRewardedPoints(
        win: Win,
        lose: Lose,
        rewardedAngle: Float
    ): Long {
        return when (rewardedAngle) {
            in FirstSectorMinAngle..FirstSectorMaxAngle -> {
                -abs(win.points) + abs(lose.points)
            }
            in SecondSectorMinAngle..SecondSectorMaxAngle -> {
                abs(win.points) * MaxMagnification
            }
            in ThirdSectorMinAngle..ThirdSectorMaxAngle -> {
                -abs(win.points)
            }
            else -> {
                abs(win.points) + abs(lose.points)
            }
        }
    }
}
