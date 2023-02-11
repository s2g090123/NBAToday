package com.jiachian.nbatoday.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.data.local.bet.Bets

data class NbaGameAndBet(
    @Embedded val game: NbaGame,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "bets_game_id"
    )
    val bets: Bets?
)
