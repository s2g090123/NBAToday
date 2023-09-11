package com.jiachian.nbatoday.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.data.local.bet.Bets

data class BetAndNbaGame(
    @Embedded val bets: Bets,
    @Relation(
        parentColumn = "bets_game_id",
        entityColumn = "game_id"
    )
    val game: NbaGame
)
