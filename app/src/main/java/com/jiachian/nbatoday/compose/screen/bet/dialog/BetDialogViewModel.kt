package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.usecase.bet.BetUseCase
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlin.math.min
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BetDialogViewModel(
    savedStateHandle: SavedStateHandle,
    private val betUseCase: BetUseCase,
    private val userUseCase: UserUseCase,
    private val gameUseCase: GameUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {
    private val gameId: String = savedStateHandle[MainRoute.BetDialog.param] ?: throw Exception("GameId is null.")

    private val stateImp = mutableStateOf(BetDialogState())
    val state: State<BetDialogState> = stateImp

    private val homePointsImp = mutableStateOf(0L)
    val homePoints: State<Long> = homePointsImp

    private val awayPointsImp = mutableStateOf(0L)
    val awayPoints: State<Long> = awayPointsImp

    private val warningImp = mutableStateOf(false)
    val warning: State<Boolean> = warningImp

    val valid = derivedStateOf { homePoints.value > 0 || awayPoints.value > 0 }

    private var user: User? = null

    init {
        getState()
    }

    private fun getState() {
        viewModelScope.launch {
            val gameBets = withContext(dispatcherProvider.io) {
                gameUseCase.getGame(gameId)
            }
            userUseCase.getUser().collect {
                stateImp.value = BetDialogState(
                    game = gameBets.game,
                    userPoints = it?.points ?: 0,
                )
                user = it
            }
        }
    }

    fun updateHomePoints(points: Long) {
        homePointsImp.value = min(points, state.value.userPoints - awayPoints.value)
    }

    fun updateAwayPoints(points: Long) {
        awayPointsImp.value = min(points, state.value.userPoints - homePoints.value)
    }

    fun updateWarning(warning: Boolean) {
        warningImp.value = warning
    }

    suspend fun bet() {
        val user = user ?: return
        betUseCase.addBet(
            user = user,
            gameId = gameId,
            homePoints = homePoints.value,
            awayPoints = awayPoints.value,
        )
    }
}
