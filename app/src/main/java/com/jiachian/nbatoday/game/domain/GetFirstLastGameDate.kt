package com.jiachian.nbatoday.game.domain

import com.jiachian.nbatoday.game.data.GameRepository
import kotlinx.coroutines.flow.first
import java.util.Date

class GetFirstLastGameDate(
    private val repository: GameRepository
) {
    suspend operator fun invoke(): Pair<Date, Date> {
        val games = repository.getGamesAndBets().first()
        return games.first().game.gameDateTime to games.last().game.gameDateTime
    }
}
