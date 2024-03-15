package com.jiachian.nbatoday.models.local.game

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.compose.screen.card.GameCardState
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.user.User
import kotlinx.coroutines.flow.Flow

data class GameAndBets(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "bet_game_id"
    )
    val bets: List<Bet>
)

fun List<GameAndBets>.toGameCardState(user: Flow<User?>): List<GameCardState> = map {
    GameCardState(it, user)
}
