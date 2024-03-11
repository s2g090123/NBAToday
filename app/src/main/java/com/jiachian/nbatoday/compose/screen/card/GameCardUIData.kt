package com.jiachian.nbatoday.compose.screen.card

import com.jiachian.nbatoday.compose.screen.bet.BetDialogViewModel
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.utils.WhileSubscribed5000
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class GameCardUIData(
    val gameAndBets: GameAndBets,
    private val betRepository: BetRepository = get(BetRepository::class.java),
    private val userRepository: UserRepository = get(UserRepository::class.java),
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main),
) {
    // logged-in user.
    private val user = userRepository.user

    // the user's points
    private val userPoints = user.map {
        it?.points ?: 0
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    // whether the user is logged in
    val login = user
        .map { it != null }
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    // the expanded status of the game card
    private val expandedImp = MutableStateFlow(false)
    val expanded = expandedImp.asStateFlow()

    // whether the game has been played
    val gamePlayed = gameAndBets.game.gamePlayed

    // whether the user has placed a bet on the game
    val hasBet = user.map { user ->
        user ?: return@map false
        gameAndBets.bets.any { it.account == user.account }
    }
        .flowOn(dispatcherProvider.io)
        .stateIn(coroutineScope, WhileSubscribed5000, true)

    // whether the user can place a bet on the game
    val betAvailable = hasBet.map {
        !gamePlayed && !it
    }.stateIn(coroutineScope, WhileSubscribed5000, true)

    private val leaders = if (gamePlayed) gameAndBets.game.gameLeaders else gameAndBets.game.teamLeaders
    val expandedContentVisible = leaders != null
    val homeLeader = leaders?.homeLeader ?: GameLeaders.GameLeader.default()
    val awayLeader = leaders?.awayLeader ?: GameLeaders.GameLeader.default()

    // the visibility of the bet dialog
    private val betDialogVisibleImp = MutableStateFlow(false)
    val betDialogVisible = betDialogVisibleImp.asStateFlow()

    /**
     * Performs user login with the provided account and password.
     *
     * @param account The user account.
     * @param password The user password.
     */
    fun login(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            userRepository.login(account, password)
        }
    }

    /**
     * Performs user registration with the provided account and password.
     *
     * @param account The user account.
     * @param password The user password.
     */
    fun register(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            userRepository.register(account, password)
        }
    }

    /**
     * Places a bet on the game with the specified home and away points.
     *
     * @param homePoints The number of points to bet on the home team.
     * @param awayPoints The number of points to bet on the away team.
     */
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
