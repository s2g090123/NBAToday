package com.jiachian.nbatoday.game.data.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.bet.data.model.local.Bet

data class GameAndBets(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "bet_game_id"
    )
    val bets: List<Bet>
)
