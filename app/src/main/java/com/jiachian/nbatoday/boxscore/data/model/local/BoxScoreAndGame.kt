package com.jiachian.nbatoday.boxscore.data.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.game.data.model.local.Game

data class BoxScoreAndGame(
    @Embedded val boxScore: BoxScore,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "game_id"
    )
    val game: Game
)
