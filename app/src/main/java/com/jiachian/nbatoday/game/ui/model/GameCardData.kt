package com.jiachian.nbatoday.game.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.local.GameLeaders
import com.jiachian.nbatoday.game.ui.event.GameCardEvent
import com.jiachian.nbatoday.home.user.data.model.local.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

data class GameCardData(
    val data: GameAndBets,
    val user: Flow<User?>,
) {
    var expanded by mutableStateOf(false)
        private set

    val betAvailable = user.map { user ->
        user ?: return@map !data.game.gamePlayed
        !data.game.gamePlayed && !data.bets.any { it.account == user.account }
    }

    private val leaders = if (data.game.gamePlayed) data.game.gameLeaders else data.game.teamLeaders
    val homeLeader = leaders?.homeLeader ?: GameLeaders.GameLeader()
    val awayLeader = leaders?.awayLeader ?: GameLeaders.GameLeader()

    private val eventImp = MutableSharedFlow<GameCardEvent>()
    val event = eventImp.asSharedFlow()

    fun updateExpanded(expanded: Boolean) {
        this.expanded = expanded
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
