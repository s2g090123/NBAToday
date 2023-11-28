package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class GameRepository : BaseRepository() {
    abstract suspend fun refreshGameBoxScore(gameId: String)

    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGameBoxScore(gameId: String): Flow<BoxScore?>
    abstract fun getGamesAndBets(): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
}
