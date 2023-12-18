package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.repository.BaseRepository
import java.util.Date
import kotlinx.coroutines.flow.Flow

abstract class GameRepository : BaseRepository() {
    abstract suspend fun insertBoxScore(gameId: String)

    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>
    abstract fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>
    abstract fun getGamesAndBets(): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getLastGameDateTime(): Flow<Date>
    abstract fun getFirstGameDateTime(): Flow<Date>
}
