package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.models.local.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

data class GameCardState(
    val data: GameAndBets,
    val user: Flow<User?>,
) {
    private val expandedImp = mutableStateOf(false)
    val expanded: State<Boolean> = expandedImp

    val betAvailable = user.map { user ->
        user ?: return@map !data.game.gamePlayed
        !data.game.gamePlayed && !data.bets.any { it.account == user.account }
    }

    private val leaders = if (data.game.gamePlayed) data.game.gameLeaders else data.game.teamLeaders
    val homeLeader = leaders?.homeLeader ?: GameLeaders.GameLeader.default()
    val awayLeader = leaders?.awayLeader ?: GameLeaders.GameLeader.default()

    private val eventImp = MutableSharedFlow<GameCardEvent>()
    val event = eventImp.asSharedFlow()

    fun updateExpanded(expanded: Boolean) {
        expandedImp.value = expanded
    }

    suspend fun attemptBet() {
        when {
            user.firstOrNull() == null -> {
                eventImp.emit(GameCardEvent.Login)
            }
            betAvailable.firstOrNull() == false -> {
                eventImp.emit(GameCardEvent.Unavailable)
            }
            else -> {
                eventImp.emit(GameCardEvent.Bet(data.game.gameId))
            }
        }
    }
}
