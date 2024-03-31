package com.jiachian.nbatoday.common.ui.bet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.bet.domain.BetUseCase
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.bet.error.BetDialogError
import com.jiachian.nbatoday.common.ui.bet.error.asBetDialogError
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogDataEvent
import com.jiachian.nbatoday.common.ui.bet.event.BetDialogUIEvent
import com.jiachian.nbatoday.common.ui.bet.state.BetDialogState
import com.jiachian.nbatoday.common.ui.bet.state.MutableBetDialogState
import com.jiachian.nbatoday.game.domain.GameUseCase
import com.jiachian.nbatoday.home.user.domain.GetUser
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.min

class BetDialogViewModel(
    savedStateHandle: SavedStateHandle,
    private val betUseCase: BetUseCase,
    private val gameUseCase: GameUseCase,
    private val getUser: GetUser,
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
                val getUser = async { getUser().first() }
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
