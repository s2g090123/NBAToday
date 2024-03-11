package com.jiachian.nbatoday.models.local.game

import androidx.room.Embedded
import androidx.room.Relation
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.models.local.bet.Bet

data class GameAndBets(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "game_id",
        entityColumn = "bet_game_id"
    )
    val bets: List<Bet>
)

fun List<GameAndBets>.toGameCardUIDataList(): List<GameCardUIData> = map {
    GameCardUIData(gameAndBets = it)
}
