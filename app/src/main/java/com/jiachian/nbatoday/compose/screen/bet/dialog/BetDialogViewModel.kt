package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.bet.dialog.event.BetDialogDataEvent
import com.jiachian.nbatoday.compose.screen.bet.dialog.event.BetDialogUIEvent
import com.jiachian.nbatoday.compose.screen.bet.dialog.state.BetDialogState
import com.jiachian.nbatoday.compose.screen.bet.dialog.state.MutableBetDialogState
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.bet.BetUseCase
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlin.math.min
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BetDialogViewModel(
    savedStateHandle: SavedStateHandle,
    private val betUseCase: BetUseCase,
    private val userUseCase: UserUseCase,
    private val gameUseCase: GameUseCase,
) : ViewModel() {
    private val gameId: String = savedStateHandle[MainRoute.BetDialog.param] ?: throw Exception("GameId is null.")

    private val stateImp = MutableBetDialogState()
    val state: BetDialogState = stateImp

    init {
        getState()
    }

    private fun getState() {
        viewModelScope.launch {
            try {
                stateImp.loading = true
                val getGame = async { gameUseCase.getGame(gameId) }
                val getUser = async { userUseCase.getUser().first() }
                val user = getUser.await()
                if (user == null) {
                    stateImp.event = BetDialogDataEvent.Error(BetDialogError.NOT_LOGIN)
                    return@launch
                }
                stateImp.apply {
                    game = getGame.await()
                    userPoints = user.points
                }
            } finally {
                stateImp.loading = false
            }
        }
    }

    private fun bet() {
        viewModelScope.launch {
            val resource = betUseCase.addBet(
                gameId = gameId,
                homePoints = state.homePoints,
                awayPoints = state.awayPoints,
            )
            when (resource) {
                is Resource.Loading -> Unit
                is Resource.Success -> stateImp.event = BetDialogDataEvent.Done
                is Resource.Error -> stateImp.event = BetDialogDataEvent.Error(resource.error.asBetDialogError())
            }
        }
    }

    fun onEvent(event: BetDialogUIEvent) {
        when (event) {
            BetDialogUIEvent.Bet -> bet()
            BetDialogUIEvent.Confirm -> stateImp.warning = true
            BetDialogUIEvent.CancelConfirm -> stateImp.warning = false
            is BetDialogUIEvent.TextAwayPoints -> stateImp.awayPoints = min(event.points, state.userPoints - state.homePoints)
            is BetDialogUIEvent.TextHomePoints -> stateImp.homePoints = min(event.points, state.userPoints - state.awayPoints)
            BetDialogUIEvent.EventReceived -> stateImp.event = null
        }
    }
}
