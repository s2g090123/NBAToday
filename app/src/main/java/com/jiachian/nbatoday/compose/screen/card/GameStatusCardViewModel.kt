package com.jiachian.nbatoday.compose.screen.card

import com.jiachian.nbatoday.compose.screen.bet.BetDialogViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.utils.getOrZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameStatusCardViewModel(
    val gameAndBet: GameAndBet,
    private val betRepository: BetRepository,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
) {
    val user = userRepository.user
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val isLogin = user.map { it != null }
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    private val isCardExpandedImp = MutableStateFlow(false)
    val isCardExpanded = isCardExpandedImp.asStateFlow()

    val isGamePlayed = gameAndBet.game.isGamePlayed

    val hasBet = user.map { user ->
        user ?: return@map false
        gameAndBet.bets.any { it.account == user.account }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)

    val canBet = hasBet.map {
        !isGamePlayed && !it
    }.stateIn(coroutineScope, SharingStarted.Eagerly, false)

    private val leaders = if (isGamePlayed) gameAndBet.game.gameLeaders else gameAndBet.game.teamLeaders
    val expandContentVisible = leaders != null
    val homeLeader = leaders?.homeLeader ?: GameLeaders.GameLeader.default()
    val awayLeader = leaders?.awayLeader ?: GameLeaders.GameLeader.default()

    private val betDialogVisibleImp = MutableStateFlow(false)
    val betDialogVisible = betDialogVisibleImp.asStateFlow()

    fun login(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            userRepository.login(account, password)
        }
    }

    fun register(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            userRepository.register(account, password)
        }
    }

    fun bet(homePoints: Long, awayPoints: Long) {
        coroutineScope.launch(dispatcherProvider.io) {
            betRepository.bet(gameAndBet.game.gameId, homePoints, awayPoints)
        }
    }

    fun showBetDialog() {
        betDialogVisibleImp.value = true
    }

    fun hideBetDialog() {
        betDialogVisibleImp.value = false
    }

    fun createBetDialogViewModel(): BetDialogViewModel {
        return BetDialogViewModel(
            gameAndBet = gameAndBet,
            userPoints = user.value?.points.getOrZero(),
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    fun setCardExpanded(expanded: Boolean) {
        isCardExpandedImp.value = expanded
    }
}
