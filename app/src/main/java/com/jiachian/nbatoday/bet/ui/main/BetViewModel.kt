package com.jiachian.nbatoday.bet.ui.main

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.bet.domain.BetUseCase
import com.jiachian.nbatoday.bet.ui.main.error.asBetError
import com.jiachian.nbatoday.bet.ui.main.event.BetDataEvent
import com.jiachian.nbatoday.bet.ui.main.event.BetUIEvent
import com.jiachian.nbatoday.bet.ui.main.state.BetState
import com.jiachian.nbatoday.bet.ui.main.state.MutableBetState
import com.jiachian.nbatoday.bet.ui.turntable.model.Lose
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus
import com.jiachian.nbatoday.bet.ui.turntable.model.Win
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.home.user.domain.UserUseCase
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.abs

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
    savedStateHandle: SavedStateHandle,
    private val betUseCase: BetUseCase,
    private val userUseCase: UserUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val account: String = savedStateHandle[MainRoute.Bet.param] ?: throw Exception("account is null.")

    // state representing the stats of [BetAndGame]
    private val stateImp = MutableBetState()
    val state: BetState = stateImp

    init {
        getBetGames()
    }

    private fun getBetGames() {
        betUseCase
            .getBetGames(account)
            .onStart { stateImp.loading = true }
            .onEach {
                Snapshot.withMutableSnapshot {
                    stateImp.apply {
                        games = it
                        loading = false
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: BetUIEvent) {
        when (event) {
            BetUIEvent.CloseTurnTable -> stateImp.turnTable.status = TurnTableStatus.Idle
            BetUIEvent.EventReceived -> stateImp.event = null
            is BetUIEvent.OpenTurnTable -> stateImp.turnTable.status = TurnTableStatus.TurnTable(event.win, event.lose)
            is BetUIEvent.Settle -> settleBet(event.betGame)
            is BetUIEvent.StartTurnTable -> startTurnTable(event.win, event.lose)
        }
    }

    private fun settleBet(betAndGame: BetAndGame) {
        viewModelScope.launch {
            val win = betAndGame.getWonPoints() * 2
            val lose = betAndGame.getLostPoints()
            val resource = userUseCase.addPoints(win)
            if (resource is Resource.Error) {
                stateImp.event = BetDataEvent.Error(resource.error.asBetError())
                return@launch
            }
            betUseCase.deleteBet(betAndGame.bet)
            stateImp.turnTable.status = TurnTableStatus.Asking(Win(win), Lose(lose))
        }
    }

    /**
     * Starts the turn table animation with the specified points configuration.
     *
     * @param win The win points configuration for the turn table.
     * @param lose The lose points configuration for the turn table.
     */
    private fun startTurnTable(win: Win, lose: Lose) {
        val status = state.turnTable.status
        if (status !is TurnTableStatus.TurnTable) {
            stateImp.turnTable.status = TurnTableStatus.Idle
            return
        }
        viewModelScope.launch(dispatcherProvider.default) {
            status.running = true
            val rewardedAngle = Random().nextInt(RandomBound).toFloat()
            val rewardedPoints = getRewardedPoints(win, lose, rewardedAngle)
            val resource = userUseCase.addPoints(rewardedPoints)
            if (resource is Resource.Error) {
                stateImp.event = BetDataEvent.Error(resource.error.asBetError())
                stateImp.turnTable.status = TurnTableStatus.Idle
                return@launch
            }
            var remainingTime = TurnTableDuration
            while (remainingTime > 0 || status.angle != rewardedAngle) {
                val currentAngle = status.angle
                val step = getTurnTableStep(remainingTime, currentAngle, rewardedAngle)
                val delay = if (remainingTime <= 0) MaxDelay else MinDelay
                delay(delay.toLong())
                remainingTime -= delay
                status.angle = if (remainingTime <= 0 && currentAngle < rewardedAngle) {
                    (currentAngle + step).coerceAtMost(rewardedAngle)
                } else {
                    (currentAngle + step) % AnglePerRound
                }
            }
            delay(ReceivedDelay)
            stateImp.turnTable.status = TurnTableStatus.Rewarded(rewardedPoints)
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
