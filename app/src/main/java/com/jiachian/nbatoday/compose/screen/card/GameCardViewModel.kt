package com.jiachian.nbatoday.compose.screen.card

import com.jiachian.nbatoday.compose.screen.bet.BetDialogViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameCardViewModel(
    val gameAndBets: GameAndBets,
    private val betRepository: BetRepository,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined),
) {
    private val user = userRepository.user

    private val userPoints = user.map {
        it?.points ?: 0
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    val login = user
        .map { it != null }
        .stateIn(coroutineScope, SharingStarted.Lazily, false)

    private val expandedImp = MutableStateFlow(false)
    val expanded = expandedImp.asStateFlow()

    val gamePlayed = gameAndBets.game.gamePlayed

    val hasBet = user.map { user ->
        user ?: return@map false
        gameAndBets.bets.any { it.account == user.account }
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, SharingStarted.Lazily, true)

    val betAvailable = hasBet.map {
        !gamePlayed && !it
    }.stateIn(coroutineScope, SharingStarted.Lazily, true)

    private val leaders = if (gamePlayed) gameAndBets.game.gameLeaders else gameAndBets.game.teamLeaders
    val expandedContentVisible = leaders != null
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
            betRepository.insertBet(gameAndBets.game.gameId, homePoints, awayPoints)
        }
    }

    fun setBetDialogVisible(visible: Boolean) {
        betDialogVisibleImp.value = visible
    }

    fun createBetDialogViewModel(): BetDialogViewModel {
        return BetDialogViewModel(
            gameAndBets = gameAndBets,
            userPoints = userPoints.value,
            dispatcherProvider = dispatcherProvider,
            coroutineScope = coroutineScope
        )
    }

    fun setCardExpanded(expanded: Boolean) {
        expandedImp.value = expanded
    }
}
