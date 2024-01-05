package com.jiachian.nbatoday.models.local.score

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.models.local.game.Game

data class BoxScoreAndGame(
    @Embedded val boxScore: BoxScore,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "game_id"
    )
    val game: Game
)
